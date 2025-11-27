package DAO;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserData
{
    private static final String DB_URL = "jdbc:sqlite:FitrakAccount.db";
    private final String SERVER_ORIGIN = Config.get("SERVER_ORIGIN");
    LocalDateTime localDateTime = LocalDateTime.now();


    public static double getTotalCaloriesBurned(long userID) {
        String sql = "SELECT SUM(caloriesBurned) AS total FROM activities WHERE userID = ?";
        double total = 0.0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total"); // ✅ use getDouble
                }
            }
        } catch (SQLException e) {
            System.err.println("Error computing total calories burned: " + e.getMessage());
        }

        return (total * 100.0)/100.0;
    }

    public static int getActivityCount( long userID, int caseType) {
        String sql = null;

        switch (caseType) {
            case 1: // all activities
                sql = "SELECT COUNT(*) AS total FROM activities WHERE userID = ?";
                break;
            case 2: // worktype has 'cardio'
                sql = "SELECT COUNT(*) AS total FROM activities WHERE userID = ? AND workType LIKE '%cardio%'";
                break;
            case 3: // worktype has 'walking' or 'basic'
                sql = "SELECT COUNT(*) AS total FROM activities WHERE userID = ? AND (workType LIKE '%walking%' OR workType LIKE '%basic%')";
                break;
            case 4: // worktype has 'strength'
                sql = "SELECT COUNT(*) AS total FROM activities WHERE userID = ? AND workType LIKE '%strength%'";
                break;
            default:
                throw new IllegalArgumentException("Invalid caseType: " + caseType);
        }

        int total = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching activity count: " + e.getMessage());
        }

        return total;
    }

    public static double getTodayCaloriesBurned(long userID) {
        String sql = "SELECT SUM(caloriesBurned) AS total " +
                "FROM activities " +
                "WHERE userID = ? AND date(startDT) = date('now','localtime')";
        double total = 0.0;

        try (Connection conn= DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error computing today's calories burned: " + e.getMessage());
        }
        return (total * 100.0)/100.0;
    }


        public static DefaultTableModel getActivitiesTableModel(long id) {
            // Match your schema column names
            String[] columnNames = {
                    "activityID", "userID", "durationMinutes", "caloriesBurned",
                    "startDT", "endDT", "metValue", "workoutType", "serverOrigin"
            };

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            String sql = "SELECT * FROM activities";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("activityID"),
                            rs.getLong("userID"),
                            rs.getDouble("durationMinutes"),
                            rs.getDouble("caloriesBurned"),
                            rs.getString("startDT"),
                            rs.getString("endDT"),
                            rs.getDouble("metValue"),
                            rs.getString("workoutType"),
                            rs.getString("serverOrigin")
                    };
                    model.addRow(row);
                }
            } catch (SQLException e) {
                System.err.println("Error fetching activities: " + e.getMessage());
            }

            return model;
        }



    public static DefaultTableModel getWorkoutsTableModelFromActivities(DefaultTableModel activitiesModel) {
        // Collect activityIDs from the activities table model
        List<Integer> activityIDs = new ArrayList<>();
        int activityIdColIndex = -1;

        // Find the column index for "activityID"
        for (int col = 0; col < activitiesModel.getColumnCount(); col++) {
            if ("activityID".equalsIgnoreCase(activitiesModel.getColumnName(col))) {
                activityIdColIndex = col;
                break;
            }
        }

        if (activityIdColIndex == -1) {
            throw new IllegalArgumentException("Activities model does not contain an 'activityID' column");
        }

        // Extract IDs
        for (int row = 0; row < activitiesModel.getRowCount(); row++) {
            Object value = activitiesModel.getValueAt(row, activityIdColIndex);
            if (value != null) {
                activityIDs.add(Integer.parseInt(value.toString()));
            }
        }

        // Define workouts table columns
        String[] columnNames = {
                "workID", "activityID", "steps", "distanceKM", "intensity",
                "calPerStep", "speedKPH", "sets", "reps",
                "currentHeartRate", "weightLifted", "serverOrigin", "logDT"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        if (activityIDs.isEmpty()) {
            return model; // return empty if no IDs
        }

        // Build dynamic IN clause
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < activityIDs.size(); i++) {
            placeholders.append("?");
            if (i < activityIDs.size() - 1) {
                placeholders.append(",");
            }
        }

        String sql = "SELECT * FROM workouts WHERE activityID IN (" + placeholders + ")";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Bind IDs
            for (int i = 0; i < activityIDs.size(); i++) {
                pstmt.setInt(i + 1, activityIDs.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("workID"),
                            rs.getInt("activityID"),
                            rs.getInt("steps"),
                            rs.getDouble("distanceKM"),
                            rs.getString("intensity"),
                            rs.getDouble("calPerStep"),
                            rs.getDouble("speedKPH"),
                            rs.getInt("sets"),
                            rs.getInt("reps"),
                            rs.getDouble("currentHeartRate"),
                            rs.getDouble("weightLifted"),
                            rs.getString("serverOrigin"),
                            rs.getString("logDT")
                    };
                    model.addRow(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching workouts: " + e.getMessage());
        }

        return model;
    }
}





