package DAO;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SyncManager extends OnlineDataBaseHelper {
    private final String DB_URL = "jdbc:sqlite:FitrakAccount.db";

    private LocalDataBaseHelper localDataBaseHelper;

    public SyncManager() {
        this.localDataBaseHelper = new LocalDataBaseHelper();
    }

    /**
     * Read all accounts from local SQLite
     */
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

    /**
     * Sync one account between SQLite and Supabase (JDBC)
     */
    public void syncAccount(Map<String, Object> sqliteAccount) throws Exception {
        try (Connection supabaseConn = getConnection()) {
            PreparedStatement stmt = supabaseConn.prepareStatement(
                    "SELECT * FROM accounts WHERE user_id = ?");
            stmt.setLong(1, (Long) sqliteAccount.get("user_id"));
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                // No Supabase record → insert new
                insertAccountToSupabase(sqliteAccount);
                logSyncEvent((Long) sqliteAccount.get("user_id"), "INSERTED", "Upload");
            } else {
                String supabaseUpdatedStr = rs.getString("last_updated_dt");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                LocalDateTime supabaseUpdated = LocalDateTime.parse(supabaseUpdatedStr, formatter);
                LocalDateTime sqliteUpdated = LocalDateTime.parse(
                        (String) sqliteAccount.get("last_updated_dt"), formatter);

                if (supabaseUpdated.isAfter(sqliteUpdated)) {
                    // Supabase newer → update SQLite
                    localDataBaseHelper.updateAll(
                            rs.getLong("user_id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("privilege"),
                            rs.getString("username"),
                            rs.getString("sex"),
                            rs.getInt("age"),
                            rs.getDouble("weight"),
                            rs.getDouble("height"),
                            rs.getDouble("bmi"),
                            rs.getString("server_origin"),
                            rs.getInt("preference"),
                            rs.getString("creation_dt"),
                            rs.getString("last_updated_dt")
                    );
                    logSyncEvent((Long) sqliteAccount.get("user_id"), "UPDATED", "PULL");

                } else if (sqliteUpdated.isAfter(supabaseUpdated)) {
                    // SQLite newer → update Supabase
                    updateSupabaseAccount(sqliteAccount);;
                    logSyncEvent((Long) sqliteAccount.get("user_id"), "UPDATED", "PUSH");

                } else {
                    System.out.println("Account already in sync for user_id: " + sqliteAccount.get("user_id"));
                }
            }
        }
    }

    /**
     * Insert new account into Supabase (JDBC)
     */
    public void insertAccountToSupabase(Map<String, Object> account) throws Exception {
        try (Connection supabaseConn = getConnection()) {
            PreparedStatement stmt = supabaseConn.prepareStatement(
                    "INSERT INTO accounts (user_id, email, password, privilege, username, sex, age, weight, height, bmi, server_origin, preference, creation_dt, last_updated_dt) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setLong(1, (Long) account.get("user_id"));
            stmt.setString(2, (String) account.get("email"));
            stmt.setString(3, (String) account.get("password"));
            stmt.setInt(4, (Integer) account.get("privilege"));
            stmt.setString(5, (String) account.get("username"));
            stmt.setString(6, (String) account.get("sex"));
            stmt.setInt(7, (Integer) account.get("age"));
            stmt.setDouble(8, (Double) account.get("weight"));
            stmt.setDouble(9, (Double) account.get("height"));
            stmt.setDouble(10, (Double) account.get("bmi"));
            stmt.setString(11, (String) account.get("server_origin"));
            stmt.setInt(12, (Integer) account.get("preference"));
            stmt.setString(13, (String) account.get("creation_dt"));
            stmt.setString(14, (String) account.get("last_updated_dt"));

            stmt.executeUpdate();
            System.out.println("Inserted account into Supabase for user_id: " + account.get("user_id"));
        }
    }

    /**
     * Update Supabase record (JDBC)
     */
    public void updateSupabaseAccount(Map<String, Object> account) throws Exception {
        try (Connection supabaseConn = getConnection()) {
            PreparedStatement stmt = supabaseConn.prepareStatement(
                    "UPDATE accounts SET email=?, password=?, privilege=?, username=?, sex=?, age=?, weight=?, height=?, bmi=?, server_origin=?, preference=?, creation_dt=?, last_updated_dt=? WHERE user_id=?");

            stmt.setString(1, (String) account.get("email"));
            stmt.setString(2, (String) account.get("password"));
            stmt.setInt(3, (Integer) account.get("privilege"));
            stmt.setString(4, (String) account.get("username"));
            stmt.setString(5, (String) account.get("sex"));
            stmt.setInt(6, (Integer) account.get("age"));
            stmt.setDouble(7, (Double) account.get("weight"));
            stmt.setDouble(8, (Double) account.get("height"));
            stmt.setDouble(9, (Double) account.get("bmi"));
            stmt.setString(10, (String) account.get("server_origin"));
            stmt.setInt(11, (Integer) account.get("preference"));
            stmt.setString(12, (String) account.get("creation_dt"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String now = LocalDateTime.now().format(formatter);
            stmt.setString(13, now);

            stmt.setLong(14, (Long) account.get("user_id"));

            stmt.executeUpdate();
            System.out.println("Updated Supabase for user_id: " + account.get("user_id"));
        }
    }
    protected void logSyncEvent(long userId, String entry, String direction) {
        String sql = "INSERT INTO syncLogs (user_id, entry, direction) VALUES (?, ?::sync_entry, ?::sync_direction)";


        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setString(2, entry);
            stmt.setString(3, direction);

            stmt.executeUpdate();
            System.out.println("Logged sync event: " + entry + " / " + direction + " for user_id " + userId);

        } catch (SQLException e) {
            System.err.println("Failed to log sync event: " + e.getMessage());
        }
    }


    /**
     * Full migration loop: SQLite → Supabase with sync
     */
    public void migrateAccounts() throws Exception {
        List<Map<String, Object>> accounts = readAccountsFromSQLite();
        for (Map<String, Object> account : accounts) {
            syncAccount(account);
        }
    }
}
