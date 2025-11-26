package DAO.test;

import DAO.Config;
import DAO.LocalDataBaseHelper;
import okhttp3.Response;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SyncWorkManager implements Runnable {
    private final LocalDataBaseHelper localHelper;
    private final SupabaseHttpClient http;

    private final String DB_URL = Config.get("SQLITE_DBURL");

    private Map<String,Object> mapWorkoutRow(ResultSet rs) throws SQLException {
        return Map.ofEntries(
                Map.entry("work_id", rs.getInt("workID")),
                Map.entry("activity_id", rs.getInt("activityID")),
                Map.entry("steps", rs.getInt("steps")),
                Map.entry("distance_km", rs.getDouble("distanceKM")),
                Map.entry("intensity", rs.getString("intensity")),
                Map.entry("cal_per_step", rs.getDouble("calPerStep")),
                Map.entry("speed_kph", rs.getDouble("speedKPH")),
                Map.entry("sets", rs.getInt("sets")),
                Map.entry("reps", rs.getInt("reps")),
                Map.entry("current_heart_rate", rs.getDouble("currentHeartRate")),
                Map.entry("weight_lifted", rs.getDouble("weightLifted")),
                Map.entry("server_origin", rs.getString("serverOrigin")),
                Map.entry("log_dt", rs.getString("logDT"))
        );
    }


    public SyncWorkManager() {
        this.localHelper = new LocalDataBaseHelper();
        this.http = new SupabaseHttpClient();
    }

    /**
     * Push local workouts that do not exist in Supabase yet.
     */
    public void pushMissingLocalWorkouts() throws Exception {
        String localSQL = "SELECT * FROM workouts";

        try (Connection localConn = DriverManager.getConnection(DB_URL);
             Statement stmt = localConn.createStatement();
             ResultSet rs = stmt.executeQuery(localSQL)) {

            while (rs.next()) {
                int workID = rs.getInt("workID");
                int activityID = rs.getInt("activityID");

                // 🔎 Check parent activity's workoutType
                PreparedStatement checkActivity = localConn.prepareStatement(
                        "SELECT workoutType FROM activities WHERE activityID = ?");
                checkActivity.setInt(1, activityID);
                ResultSet actRS = checkActivity.executeQuery();

                if (actRS.next()) {
                    String workoutType = actRS.getString("workoutType");
                    if ("DELETED".equalsIgnoreCase(workoutType)) {
                        System.out.println("Skipping workout ID=" + workID +
                                " because parent activityID=" + activityID +
                                " is marked DELETED");
                        continue; // ⛔ skip push
                    }
                }

                // ✅ Only push if parent activity is not DELETED
                try (Response resp = http.get("/rest/v1/workouts?work_id=eq." + workID)) {
                    String body = resp.body().string();
                    if (body.equals("[]")) {

                        Map<String,Object> workoutRow = mapWorkoutRow(rs);
                        String payload = Json.toJson(workoutRow);

                        try (Response insertResp = http.post("/rest/v1/workouts", payload)) {
                            if (!insertResp.isSuccessful()) {
                                String err = insertResp.body() != null ? insertResp.body().string() : "";
                                throw new RuntimeException("Failed to push workout " + workID + ": " + err);
                            }
                            System.out.println("Pushed local workout ID=" + workID);
                            SyncAccManager syncAccManager = new SyncAccManager();
                            syncAccManager.logSyncEvent(0, activityID, workID,
                                    SyncAccManager.SyncEntry.INSERTED,
                                    SyncAccManager.SyncDirection.PUSH,
                                    SyncAccManager.TableRef.WORKOUT);
                        }
                    }
                }
            }
        }
    }


    /**
     * Pull workouts from Supabase that are missing locally.
     */
    public void pullMissingOnlineWorkouts() throws Exception {
        try (Response resp = http.get("/rest/v1/workouts?select=*")) {
            String body = resp.body().string();
            List<Map<String,Object>> onlineWorkouts = Json.fromJsonList(body);

            try (Connection localConn = DriverManager.getConnection(DB_URL)) {
                for (Map<String,Object> workout : onlineWorkouts) {
                    int workID = ((Number) workout.get("work_id")).intValue();
                    int activityID = ((Number) workout.get("activity_id")).intValue();

                    // 🔎 Check parent activity's workoutType
                    PreparedStatement checkActivity = localConn.prepareStatement(
                            "SELECT workoutType FROM activities WHERE activityID = ?");
                    checkActivity.setInt(1, activityID);
                    ResultSet actRS = checkActivity.executeQuery();

                    if (actRS.next()) {
                        String workoutType = actRS.getString("workoutType");
                        if ("DELETED".equalsIgnoreCase(workoutType)) {
                            System.out.println("Skipping workout ID=" + workID +
                                    " because parent activityID=" + activityID +
                                    " is marked DELETED");
                            continue; // ⛔ skip insert
                        }
                    }

                    // ✅ Check if workout already exists locally
                    PreparedStatement check = localConn.prepareStatement(
                            "SELECT workID FROM workouts WHERE workID = ?");
                    check.setInt(1, workID);
                    ResultSet localRS = check.executeQuery();

                    if (!localRS.next()) {
                        PreparedStatement insert = localConn.prepareStatement(
                                "INSERT INTO workouts (workID, activityID, steps, distanceKM, intensity, calPerStep, speedKPH, sets, reps, currentHeartRate, weightLifted, serverOrigin, logDT) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                        insert.setInt(1, workID);
                        insert.setInt(2, activityID);
                        insert.setInt(3, ((Number) workout.get("steps")).intValue());
                        insert.setDouble(4, ((Number) workout.get("distance_km")).doubleValue());
                        insert.setString(5, (String) workout.get("intensity"));
                        insert.setDouble(6, ((Number) workout.get("cal_per_step")).doubleValue());
                        insert.setDouble(7, ((Number) workout.get("speed_kph")).doubleValue());
                        insert.setInt(8, ((Number) workout.get("sets")).intValue());
                        insert.setInt(9, ((Number) workout.get("reps")).intValue());
                        insert.setDouble(10, ((Number) workout.get("current_heart_rate")).doubleValue());
                        insert.setDouble(11, ((Number) workout.get("weight_lifted")).doubleValue());
                        insert.setString(12, (String) workout.get("server_origin"));
                        insert.setString(13, (String) workout.get("log_dt"));

                        insert.executeUpdate();

                        System.out.println("Pulled online workout ID=" + workID);
                        SyncAccManager syncAccManager = new SyncAccManager();
                        syncAccManager.logSyncEvent(0, activityID, workID,
                                SyncAccManager.SyncEntry.INSERTED,
                                SyncAccManager.SyncDirection.PULL,
                                SyncAccManager.TableRef.WORKOUT);
                    }
                }
            }
        }
    }

    /**
     * Resolve workouts marked DELETED online.
     */
    public void resolveDeletedWorkouts() throws Exception {
        // Pull activities marked DELETED from Supabase
        try (Response resp = http.get("/rest/v1/activities?workout_type=eq.DELETED")) {
            String body = resp.body().string();
            List<Map<String,Object>> deletedActivities = Json.fromJsonList(body);

            try (Connection localConn = DriverManager.getConnection(DB_URL)) {
                for (Map<String,Object> activity : deletedActivities) {
                    int activityID = ((Number) activity.get("activity_id")).intValue();

                    // Delete all workouts referencing this activityID
                    PreparedStatement deleteCascade = localConn.prepareStatement(
                            "DELETE FROM workouts WHERE activityID = ?");
                    deleteCascade.setInt(1, activityID);
                    int affected = deleteCascade.executeUpdate();

                    if (affected > 0) {
                        System.out.println("Deleted local workouts referencing activityID=" + activityID +
                                " (activity marked DELETED online)");
                        SyncAccManager syncAccManager = new SyncAccManager();
                        syncAccManager.logSyncEvent(0, activityID, 0,
                                SyncAccManager.SyncEntry.DELETED,
                                SyncAccManager.SyncDirection.PULL,
                                SyncAccManager.TableRef.WORKOUT);
                    }
                }
            }
        }
    }

    /**
     * Run full sync cycle.
     */
    public void syncAllWorkouts() throws Exception {
        pushMissingLocalWorkouts();
        pullMissingOnlineWorkouts();
        resolveDeletedWorkouts();
    }

    @Override
    public void run() {
        try {
            syncAllWorkouts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startWorkSyncThread() {
        Thread thread = new Thread(this);
        thread.start();
    }
}
