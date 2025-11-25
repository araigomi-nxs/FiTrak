package DAO.test;

import DAO.Config;
import DAO.LocalDataBaseHelper;
import okhttp3.Response;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SyncActManager implements Runnable {
    private final LocalDataBaseHelper localHelper;
    private final SupabaseHttpClient http;

    private final String DB_URL = Config.get("SQLITE_DBURL");

    public SyncActManager() {
        this.localHelper = new LocalDataBaseHelper();
        this.http = new SupabaseHttpClient();

    }

    /**
     * Push local activities that do not exist in Supabase yet.
     */
    public void pushMissingLocalActivities() throws Exception {
        String localSQL = "SELECT * FROM activities";

        try (Connection localConn = DriverManager.getConnection(DB_URL);
             Statement stmt = localConn.createStatement();
             ResultSet rs = stmt.executeQuery(localSQL)) {

            while (rs.next()) {
                int activityID = rs.getInt("activityID");

                // Check if activity exists online (Supabase)
                try (Response resp = http.get("/rest/v1/activities?activity_id=eq." + activityID)) {
                    String body = resp.body().string();
                    if (body.equals("[]")) {
                        // Not found → insert into Supabase
                        Map<String,Object> activityRow = Map.of(
                                "activity_id", activityID,
                                "user_id", rs.getLong("userID"),
                                "duration_minutes", rs.getDouble("durationMinutes"),
                                "calories_burned", rs.getDouble("caloriesBurned"),
                                "start_dt", rs.getString("startDT"),
                                "end_dt", rs.getString("endDT"),
                                "met_value", rs.getDouble("metValue"),
                                "workout_type", rs.getString("workoutType"),
                                "server_origin", rs.getString("serverOrigin"),
                                "initial_weight", rs.getDouble("initialWeight")
                        );

                        String payload = Json.toJson(activityRow);
                        try (Response insertResp = http.post("/rest/v1/activities", payload)) {
                            if (!insertResp.isSuccessful()) {
                                String err = insertResp.body() != null ? insertResp.body().string() : "";
                                throw new RuntimeException("Failed to push activity " + activityID + ": " + err);
                            }
                            System.out.println("Pushed local activity ID=" + activityID);
                            SyncAccManager syncAccManager = new SyncAccManager();
                           syncAccManager.logSyncEvent(0, activityID, 0, SyncAccManager.SyncEntry.INSERTED, SyncAccManager.SyncDirection.PUSH, SyncAccManager.TableRef.ACTIVITY);
                        }
                    }
                }
            }
        }
    }
    public void pullMissingOnlineActivities() throws Exception {
        try (Response resp = http.get("/rest/v1/activities?select=*")) {
            String body = resp.body().string();
            List<Map<String,Object>> onlineActivities = Json.fromJsonList(body);

            try (Connection localConn = DriverManager.getConnection(DB_URL)) {
                for (Map<String,Object> activity : onlineActivities) {
                    int activityID = ((Number) activity.get("activity_id")).intValue();
                    String workoutType = (String) activity.get("workout_type");

                    if ("DELETED".equalsIgnoreCase(workoutType)) {
                        System.out.println("Skipping DELETED activity from Supabase: " + activityID);
                        continue;
                    }


                    // Check if exists locally
                    PreparedStatement check = localConn.prepareStatement(
                            "SELECT activityID FROM activities WHERE activityID = ?");
                    check.setInt(1, activityID);
                    ResultSet localRS = check.executeQuery();

                    if (!localRS.next()) {
                        PreparedStatement insert = localConn.prepareStatement(
                                "INSERT INTO activities (activityID, userID, durationMinutes, caloriesBurned, startDT, endDT, metValue, initialWeight, workoutType, serverOrigin) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                        insert.setInt(1, activityID);
                        insert.setLong(2, ((Number) activity.get("user_id")).longValue());
                        insert.setDouble(3, ((Number) activity.get("duration_minutes")).doubleValue());
                        insert.setDouble(4, ((Number) activity.get("calories_burned")).doubleValue());
                        insert.setString(5, (String) activity.get("start_dt"));
                        insert.setString(6, (String) activity.get("end_dt"));
                        insert.setDouble(7, ((Number) activity.get("met_value")).doubleValue());
                        insert.setDouble(8, ((Number) activity.get("initial_weight")).doubleValue());
                        insert.setString(9, workoutType);
                        insert.setString(10, (String) activity.get("server_origin"));

                        insert.executeUpdate();

                        System.out.println("Pulled online activity ID=" + activityID);
                        SyncAccManager syncAccManager = new SyncAccManager();
                        syncAccManager.logSyncEvent(0, activityID, 0, SyncAccManager.SyncEntry.INSERTED, SyncAccManager.SyncDirection.PULL, SyncAccManager.TableRef.ACTIVITY);

                    }
                }
            }
        }
    }
    public void resolveDeletedActivities() throws Exception {
        try (Response resp = http.get("/rest/v1/activities?workout_type=eq.DELETED")) {
            String body = resp.body().string();
            List<Map<String,Object>> deletedActivities = Json.fromJsonList(body);

            try (Connection localConn = DriverManager.getConnection(DB_URL)) {
                for (Map<String,Object> activity : deletedActivities) {
                    int activityID = ((Number) activity.get("activity_id")).intValue();

                    PreparedStatement delete = localConn.prepareStatement(
                            "DELETE FROM activities WHERE activityID = ?");
                    delete.setInt(1, activityID);
                    int affected = delete.executeUpdate();

                    if (affected > 0) {
                        System.out.println("Deleted local activity ID=" + activityID + " (marked DELETED online)");
                        SyncAccManager syncAccManager = new SyncAccManager();
                        syncAccManager.logSyncEvent(0, activityID, 0, SyncAccManager.SyncEntry.DELETED, SyncAccManager.SyncDirection.PULL, SyncAccManager.TableRef.ACTIVITY);
                    }
                }
            }
        }
    }

    /**
     * Run full sync cycle.
     */
    public void syncAllActivities() throws Exception {
        pushMissingLocalActivities();
        pullMissingOnlineActivities();
        resolveDeletedActivities();
    }

    @Override
    public void run() {
        try {
            syncAllActivities();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startActSyncThread() {
        Thread thread = new Thread(this);
        thread.start();
    }

}