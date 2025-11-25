package DAO.test;
import DAO.Config;
import DAO.LocalDataBaseHelper;
import okhttp3.Response;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SyncAccManager implements Runnable {
    private final LocalDataBaseHelper localDataBaseHelper;
    private final SupabaseHttpClient http;
    protected static final DateTimeFormatter formatter =         DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    // Load values from config.properties
    private final String DB_URL = Config.get("SQLITE_DBURL");
    private static final String SERVER_ORIGIN = Config.get("SERVER_ORIGIN");
    private final String DATE_FORMAT = Config.get("DATE_FORMAT");

    public enum SyncEntry {
        UPDATED,
        INSERTED,
        DELETED
    }

    public enum SyncDirection {
        PUSH,
        PULL
    }

    public enum TableRef {
        ACCOUNT,
        ACTIVITY,
        WORKOUT
    }

    public SyncAccManager() {
        this.localDataBaseHelper = new LocalDataBaseHelper();
        this.http = new SupabaseHttpClient();
    }

    private Map<String, Object> fetchSupabaseAccount(SupabaseHttpClient http, long userId) throws Exception {
        // eq filter; select=*; limit=1
        try (Response resp = http.get("/rest/v1/accounts?user_id=eq." + userId + "&select=*&limit=1")) {
            if (!resp.isSuccessful()) throw new RuntimeException("Fetch failed: " + resp.code() + " " + resp.message());
            String body = resp.body().string();
            // Response is a JSON array; parse first element if present
            var list = Json.fromJson(body, java.util.List.class);
            if (list instanceof java.util.List && !((java.util.List<?>) list).isEmpty()) {
                return (Map<String, Object>) ((java.util.List<?>) list).get(0);
            }
            return null;
        }


    }

    private void insertAccountToSupabaseHttp(SupabaseHttpClient http, Map<String, Object> account) throws Exception {
        // PostgREST insert; prefer ISO 8601 UTC strings for timestamps
        String payload = Json.toJson(account);
        try (Response resp = http.post("/rest/v1/accounts", payload)) {
            if (!resp.isSuccessful()) throw new RuntimeException("Insert failed: " + resp.code() + " " + resp.message());
        }
    }
    public void updateSupabaseAccountHttp(Map<String,Object> account) throws Exception {
        SupabaseHttpClient http = new SupabaseHttpClient();
        long userId = (Long) account.get("user_id");

        // bump last_updated_dt to now
        String now = java.time.LocalDateTime.now().toString();
        account.put("last_updated_dt", now);

        String payload = Json.toJson(account); // use Jackson/Gson
        try (Response resp = http.patch("/rest/v1/accounts?user_id=eq." + userId, payload)) {
            if (!resp.isSuccessful()) throw new RuntimeException("Update failed: " + resp.code());
            System.out.println("Updated Supabase for user_id: " + userId);
        }
    }
    public void syncOnlineAccountHttp() throws Exception {
        SupabaseHttpClient http = new SupabaseHttpClient();
        try (Response resp = http.get("/rest/v1/accounts?select=*")) {
            String body = resp.body().string();
            List<Map<String,Object>> accounts = Json.fromJson(body, List.class);

            for (Map<String,Object> acc : accounts) {
                long userId = ((Number) acc.get("user_id")).longValue();
                int privilege = ((Number) acc.get("privilege")).intValue();

                if (privilege == -1) continue;

                try (Connection localConn = DriverManager.getConnection(DB_URL);
                     PreparedStatement localStmt = localConn.prepareStatement("SELECT * FROM accounts WHERE userID=?")) {
                    localStmt.setLong(1, userId);
                    ResultSet localRs = localStmt.executeQuery();

                    if (!localRs.next()) {
                        localDataBaseHelper.insertUser(
                                userId,
                                (String) acc.get("email"),
                                (String) acc.get("password"),
                                privilege,
                                (String) acc.get("username"),
                                (String) acc.get("sex"),
                                ((Number) acc.get("age")).intValue(),
                                ((Number) acc.get("weight")).doubleValue(),
                                ((Number) acc.get("height")).doubleValue(),
                                ((Number) acc.get("bmi")).doubleValue(),
                                (String) acc.get("server_origin"),
                                ((Number) acc.get("preference")).intValue(),
                                (String) acc.get("creation_dt"),
                                (String) acc.get("last_updated_dt")
                        );
                        logSyncEvent(userId,0,0,SyncEntry.INSERTED,SyncDirection.PULL,TableRef.ACCOUNT);
                    }
                }
            }
        }
    }

    public void syncLocalAccount(Map<String, Object> sqliteAccount) throws Exception {
        SupabaseHttpClient http = new SupabaseHttpClient();

        Map<String, Object> supabaseAccount = fetchSupabaseAccount(http, (Long) sqliteAccount.get("user_id"));

        if (supabaseAccount == null) {
            insertAccountToSupabaseHttp(http, sqliteAccount);
            logSyncEvent((Long) sqliteAccount.get("user_id"), 0, 0, SyncEntry.INSERTED, SyncDirection.PUSH, TableRef.ACCOUNT);
            return;
        }

        // Both sides store last_updated_dt as strings
        String supabaseDateStr = (String) supabaseAccount.get("last_updated_dt");
        String sqliteDateStr   = (String) sqliteAccount.get("last_updated_dt");

        // Convert string → LocalDateTime using space separator

        LocalDateTime supabaseUpdated = LocalDateTime.parse(supabaseDateStr, formatter);
        LocalDateTime sqliteUpdated   = LocalDateTime.parse(sqliteDateStr, formatter);

        if (supabaseUpdated.isAfter(sqliteUpdated)) {
            // Supabase newer → update local
            localDataBaseHelper.updateAll(
                    (Long) supabaseAccount.get("user_id"),
                    (String) supabaseAccount.get("email"),
                    (String) supabaseAccount.get("password"),
                    ((Number) supabaseAccount.get("privilege")).intValue(),
                    (String) supabaseAccount.get("username"),
                    (String) supabaseAccount.get("sex"),
                    ((Number) supabaseAccount.get("age")).intValue(),
                    ((Number) supabaseAccount.get("weight")).doubleValue(),
                    ((Number) supabaseAccount.get("height")).doubleValue(),
                    ((Number) supabaseAccount.get("bmi")).doubleValue(),
                    (String) supabaseAccount.get("server_origin"),
                    ((Number) supabaseAccount.get("preference")).intValue(),
                    (String) supabaseAccount.get("creation_dt"),
                    supabaseDateStr // keep original string
            );
            logSyncEvent((Long) sqliteAccount.get("user_id"), 0, 0, SyncEntry.UPDATED, SyncDirection.PUSH, TableRef.ACCOUNT);

        } else if (sqliteUpdated.isAfter(supabaseUpdated)) {
            // SQLite newer → update Supabase
            String nowStr = LocalDateTime.now().format(formatter);
            sqliteAccount.put("last_updated_dt", nowStr);

            updateSupabaseAccountHttp(sqliteAccount);
            logSyncEvent((Long) sqliteAccount.get("user_id"), 0, 0, SyncEntry.UPDATED, SyncDirection.PUSH, TableRef.ACCOUNT);

        } else {
            System.out.println("Account already in sync for user_id: " + sqliteAccount.get("user_id"));
        }
    }





    public void deleteLocalInvalidAccountsHttp() throws Exception {
        SupabaseHttpClient http = new SupabaseHttpClient();
        try (Response resp = http.get("/rest/v1/accounts?select=user_id,privilege")) {
            String body = resp.body().string();
            List<Map<String,Object>> accounts = Json.fromJson(body, List.class);

            for (Map<String,Object> acc : accounts) {
                long userId = ((Number) acc.get("user_id")).longValue();
                int privilege = ((Number) acc.get("privilege")).intValue();

                if (privilege == -1) {
                    try (Connection localConn = DriverManager.getConnection(DB_URL);
                         PreparedStatement deleteStmt = localConn.prepareStatement("DELETE FROM accounts WHERE userID=?")) {
                        deleteStmt.setLong(1, userId);
                        int rowsDeleted = deleteStmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            logSyncEvent(userId,0,0,SyncEntry.UPDATED,SyncDirection.PUSH,TableRef.ACCOUNT);
                        }
                    }
                }
            }
        }
    }

    public void logSyncEvent(long userId, int actID, int workID,
                             SyncEntry entry, SyncDirection direction, TableRef tableRef) {
        try {
            // Format current time as a string (safe for Supabase)
            String now = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            Map<String,Object> logRow = Map.of(
                    "user_id", userId,
                    "activity_id", actID,
                    "workout_id", workID,
                    "sync_entry", entry.name(),
                    "sync_direction", direction.name(),
                    "log_date", now,   // plain string
                    "server_origin", SERVER_ORIGIN,
                    "table_ref", tableRef.name()
            );

            String payload = Json.toJson(logRow);
            try (Response resp = http.post("/rest/v1/sync_logs", payload)) {
                if (!resp.isSuccessful()) {
                    String body = resp.body() != null ? resp.body().string() : "";
                    throw new RuntimeException("Log insert failed: " + resp.code() + " " + body);
                }
                System.out.println("LOG sync event: " + entry + " / " + direction +
                        ((userId != 0) ? "USR:" + userId : ((actID!=0)? "ACT:" + actID: "WORK:" + workID)) +
                        " from " + SERVER_ORIGIN + ":" + tableRef);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSyncLogsHttp(JTextArea textArea) {
        SupabaseHttpClient http = new SupabaseHttpClient();
        try (Response resp = http.get(
                "/rest/v1/sync_logs?select=log_id,user_id,activity_id,workout_id,sync_entry,sync_direction,log_date,server_origin,table_ref&order=log_date.desc")) {

            if (!resp.isSuccessful()) {
                String err = resp.body() != null ? resp.body().string() : "";
                throw new RuntimeException("Logs fetch failed: " + resp.code() + " " + err);
            }

            String body = resp.body().string();
            List<Map<String,Object>> logs = Json.fromJsonList(body);

            StringBuilder sb = new StringBuilder();
            for (Map<String,Object> row : logs) {
                sb.append("Log #").append(row.get("log_id"))
                        .append(" |USR: ").append(row.get("user_id"))
                        .append(" |ACT: ").append(row.get("activity_id"))
                        .append(" |WORK: ").append(row.get("workout_id"))
                        .append(" |OP: ").append(row.get("sync_entry")).append(":").append(row.get("sync_direction"))
                        .append(" |DT: ").append(row.get("log_date"))
                        .append(" |SRC: ").append(row.get("server_origin"))
                        .append(" |TBL: ").append(row.get("table_ref"))
                        .append("\n");
            }

            SwingUtilities.invokeLater(() -> textArea.setText(sb.toString()));
        } catch (Exception e) {
            System.err.println("Failed to fetch sync logs: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> readAccountsFromSQLite() throws Exception {
        List<Map<String, Object>> accounts = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM accounts")) {

            while (rs.next()) {
                Map<String, Object> account = new HashMap<>();
                account.put("user_id", rs.getLong("userID"));
                account.put("email", rs.getString("email"));
                account.put("password", rs.getString("password"));
                account.put("privilege", rs.getInt("privilege"));
                account.put("username", rs.getString("username"));
                account.put("sex", rs.getString("sex"));
                account.put("age", rs.getInt("age"));
                account.put("weight", rs.getDouble("weight"));
                account.put("height", rs.getDouble("height"));
                account.put("bmi", rs.getDouble("BMI"));
                account.put("server_origin", rs.getString("serverOrigin"));
                account.put("preference", rs.getInt("preference"));
                account.put("creation_dt", rs.getString("creationDT"));
                account.put("last_updated_dt", rs.getString("lastUpdatedDT"));
                accounts.add(account);
            }
        }
        return accounts;
    }

    public void compareAccountsODBHttp() throws Exception {
        // Step 1: Read all accounts from local SQLite
        List<Map<String, Object>> accounts = readAccountsFromSQLite();
        // Step 2: For each local account, sync with Supabase
        for (Map<String, Object> account : accounts) {
            syncLocalAccount(account); // uses HTTP fetch/insert/update
        }
        // Step 3: Delete local accounts if Supabase marks them invalid
        deleteLocalInvalidAccountsHttp();
        // Step 4: Pull missing Supabase accounts into local SQLite
        syncOnlineAccountHttp();
    }


    @Override
    public void run() {
        try {
            compareAccountsODBHttp(); // your HTTP sync loop
        } catch (Exception e) {
            System.err.println("Sync thread failed: " + e.getMessage());
        }
    }

    public void startSyncThread() {
        Thread thread = new Thread(this);
        thread.start();
    }


}
