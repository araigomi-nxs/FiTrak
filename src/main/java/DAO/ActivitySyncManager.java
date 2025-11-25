package DAO;

import java.sql.*;
import java.util.*;

public class ActivitySyncManager extends OnlineDataBaseHelper implements Runnable {
    private final String DB_URL = "jdbc:sqlite:FitrakAccount.db";
    private final AccountSyncManager accountsSyncManager = new AccountSyncManager();

    private LocalDataBaseHelper localHelper;


    public ActivitySyncManager() {
        this.localHelper = new LocalDataBaseHelper();

    }


    public void pushMissingLocalActivities() throws Exception {
        String localSQL = "SELECT * FROM activities";
        try (Connection localConn = DriverManager.getConnection(DB_URL);
             Statement stmt = localConn.createStatement();
             ResultSet rs = stmt.executeQuery(localSQL)) {

            while (rs.next()) {
                int activityID = rs.getInt("activityID");

                try (Connection supabaseConn = getConnection()) {
                    PreparedStatement check = supabaseConn.prepareStatement(
                            "SELECT activity_id FROM activities WHERE activity_id = ?");
                    check.setInt(1, activityID);
                    ResultSet supabaseRS = check.executeQuery();

                    if (!supabaseRS.next()) {
                        PreparedStatement insert = supabaseConn.prepareStatement(
                                "INSERT INTO activities (activity_id, user_id, duration_minutes, calories_burned, start_dt, end_dt, met_value, workout_type, server_origin, initial_weight) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                        insert.setInt(1, activityID);
                        insert.setLong(2, rs.getLong("userID"));
                        insert.setDouble(3, rs.getDouble("durationMinutes"));
                        insert.setDouble(4, rs.getDouble("caloriesBurned"));
                        insert.setString(5, rs.getString("startDT"));
                        insert.setString(6, rs.getString("endDT"));
                        insert.setDouble(7, rs.getDouble("metValue"));
                        insert.setString(8, rs.getString("workoutType"));
                        insert.setString(9, rs.getString("serverOrigin"));
                        insert.setDouble(10, rs.getDouble("initialWeight"));

                        insert.executeUpdate();

                        accountsSyncManager.logSyncEvent(0, activityID ,0,"INSERTED", "PUSH", "ACTIVITY");
                    }
                }
            }
        }
    }

    /**
     * Pull Supabase activities into local if not yet present
     */
    public void pullMissingOnlineActivities() throws Exception {
        String supabaseSQL = "SELECT * FROM activities";
        try (Connection supabaseConn = getConnection();
             Statement stmt = supabaseConn.createStatement();
             ResultSet rs = stmt.executeQuery(supabaseSQL)) {

            while (rs.next()) {
                int activityID = rs.getInt("activity_id");
                String workoutType = rs.getString("workout_type");

                if ("DELETED".equalsIgnoreCase(workoutType)) {
                    System.out.println("Skipping DELETED activity from Supabase: " + activityID);
                    continue;
                }

                try (Connection localConn = DriverManager.getConnection(DB_URL)) {
                    PreparedStatement check = localConn.prepareStatement(
                            "SELECT activityID FROM activities WHERE activityID = ?");
                    check.setInt(1, activityID);
                    ResultSet localRS = check.executeQuery();

                    if (!localRS.next()) {
                        PreparedStatement insert = localConn.prepareStatement(
                                "INSERT INTO activities (activityID, userID, durationMinutes, caloriesBurned, startDT, endDT, metValue, workoutType, serverOrigin, initialWeight) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                        insert.setLong(1, activityID);
                        insert.setLong(2, rs.getLong("user_id"));
                        insert.setDouble(3, rs.getDouble("duration_minutes"));
                        insert.setDouble(4, rs.getDouble("calories_burned"));
                        insert.setString(5, rs.getString("start_dt"));
                        insert.setString(6, rs.getString("end_dt"));
                        insert.setDouble(7, rs.getDouble("met_value"));
                        insert.setString(8, workoutType); // safe reuse
                        insert.setString(9, rs.getString("server_origin"));
                        insert.setDouble(10, rs.getDouble("initial_weight"));

                        insert.executeUpdate();

                        accountsSyncManager.logSyncEvent(0,activityID,0, "INSERTED", "PULL", "ACTIVITY");
                    }
                }
            }
        }
    }


    /**
     * Remove local activities if Supabase marks them as DELETED
     */
    public void resolveDeletedActivities() throws Exception {
        String sql = "SELECT activity_id, user_id FROM activities WHERE workout_type = 'DELETED'";
        try (Connection supabaseConn = getConnection();
             PreparedStatement stmt = supabaseConn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int activityID = rs.getInt("activity_id");
                long userID = rs.getLong("user_id");

                try (Connection localConn = DriverManager.getConnection(DB_URL)) {
                    PreparedStatement delete = localConn.prepareStatement(
                            "DELETE FROM activities WHERE activityID = ?");
                    delete.setInt(1, activityID);
                    int affected = delete.executeUpdate();

                    if (affected > 0) {

                        accountsSyncManager.logSyncEvent(0, activityID,0, "DELETED", "PULL", "ACTIVITY");
                    }
                }
            }
        }
    }

    public void syncAllActivities() throws Exception {
        pushMissingLocalActivities();
        pullMissingOnlineActivities();
        resolveDeletedActivities();
    }

    @Override
    public void run() {
        try {
            syncAllActivities(); // Your sync logic here
        } catch (Exception e) {
            System.err.println("Sync thread failed: " + e.getMessage());
        }
    }
    public void startActSyncThread() {
        Thread thread = new Thread(this);
        thread.start();
    }


}
