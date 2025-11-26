package DAO;

import org.sqlite.core.DB;

import java.util.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class LocalActDBHelper {


    private final String DB_URL = "jdbc:sqlite:FitrakAccount.db";

    public LocalActDBHelper() {

        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Connected to SQLite database.");
                initializeSchema(conn);
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    private static void initializeSchema(Connection conn) {
        String sql =  """
                CREATE TABLE IF NOT EXISTS activities (
                activityID INTEGER PRIMARY KEY AUTOINCREMENT, 
                userID INTEGER NOT NULL, 
                durationMinutes REAL,
                caloriesBurned REAL,
                startDT TEXT NOT NULL,
                endDT TEXT NOT NULL,
                metValue REAL,
                workoutType TEXT,
                serverOrigin TEXT,
                
                FOREIGN KEY (userID) REFERENCES accounts(userID)
                ); 
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
        }



    }

    public long insertActivity(long userID, double durationMinutes, double caloriesBurned, String startDT, String endDT, double metValue,double initialWeight,  String workoutType, String serverOrigin )
    {
        String sql = "INSERT INTO activities (userID, durationMinutes, caloriesBurned, startDT,  endDT, metValue,initialWeight,workoutType,  serverOrigin) VALUES (?,?,?,?,?,?,?,?,?)";

        long activityID = -1;

        try( Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setLong(1, userID);
            pstmt.setDouble(2, durationMinutes);
            pstmt.setDouble(3, caloriesBurned);
            pstmt.setString(4, startDT);
            pstmt.setString(5, endDT);
            pstmt.setDouble(6, metValue);
            pstmt.setDouble(7, initialWeight);
            pstmt.setString(8, workoutType);
            pstmt.setString(9, serverOrigin);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        activityID = rs.getLong(1); // retrieve the auto-generated key
                    }
                }
            }

            System.out.println("Activities inserted successfully");
        }
        catch (SQLException e) {
            System.err.println("Insert  failed: " + e.getMessage());
        }
        return activityID;
    }


    public DefaultTableModel getActivitiesTable() {
        String[] columnNames = {"activityID", "userID", "workoutType","durMin", "calBurn", "startDT", "endDT", "metValue","initialWeight",  "serverOrigin"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);


        String sql = "SELECT * FROM activities";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("activityID"),
                        rs.getLong("userID"),
                        rs.getString("workoutType"),
                        rs.getDouble("durationMinutes"),
                        rs.getDouble("caloriesBurned"),
                        rs.getString("startDT"),
                        rs.getString("endDT"),
                        rs.getDouble("metValue"),
                        rs.getDouble("initialWeight"),
                        rs.getString("serverOrigin")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            System.err.println("Data fetch failed: " + e.getMessage());
        }

        return model;
    }

    public  DefaultTableModel searchByUserID(long userID) {
        String sql = "SELECT * FROM activities WHERE userID = ?";

        // Define column names matching your schema
        String[] columnNames = {
                "activityID", "userID", "durationMinutes", "caloriesBurned",
                "startDT", "endDT", "metValue", "workoutType", "serverOrigin"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);


        if (userID == 0) {
            System.out.println("Invalid userID (0), returning default model.");
            return getActivitiesTable();
        }


        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userID);

            try (ResultSet rs = pstmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }

        return model;
    }

    public List<Object[]> getWorkoutData(String workoutType, int activityID) {
        List<Object[]> workoutData = new ArrayList<>();
        String sql = "";

        // Choose SQL based on workoutType
        switch (workoutType) {
            case "Walking" -> sql = """
            SELECT steps, distanceKM, intensity, calPerStep, logDT
            FROM workouts WHERE activityID = ?
        """;

            case "Running" -> sql = """
            SELECT speedKPH, distanceKM, intensity, terrain, logDT
            FROM workouts WHERE activityID = ?
        """;

            case "Cycling" -> sql = """
            SELECT speedKPH, distanceKM, intensity, '' AS terrain, logDT
            FROM workouts WHERE activityID = ?
        """;

            case "Cardio:Burpees", "Cardio:JumpingJacks", "Cardio:JumpRope" -> sql = """
            SELECT sets, reps, intensity, currentHeartRate, logDT
            FROM workouts WHERE activityID = ?
        """;

            case "Strength:Leg", "Strength:Pull", "Strength:Push" -> sql = """
            SELECT sets, reps, intensity, weightLifted, logDT
            FROM workouts WHERE activityID = ?
        """;

            default -> {
                System.err.println("Unsupported workout type: " + workoutType);
                return workoutData;
            }
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, activityID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row;

                    switch (workoutType) {
                        case "Walking" -> row = new Object[]{
                                rs.getInt("steps"),
                                rs.getDouble("distanceKM"),
                                rs.getString("intensity"),
                                rs.getDouble("calPerStep"),
                                rs.getString("logDT")
                        };

                        case "Running" -> row = new Object[]{
                                rs.getDouble("speedKPH"),
                                rs.getDouble("distanceKM"),
                                rs.getString("intensity"),
                                rs.getString("terrain"),
                                rs.getString("logDT")
                        };

                        case "Cycling" -> row = new Object[]{
                                rs.getDouble("speedKPH"),
                                rs.getDouble("distanceKM"),
                                rs.getString("intensity"),
                                "", // terrain placeholder
                                rs.getString("logDT")
                        };

                        case "Cardio:Burpees", "Cardio:JumpingJacks", "Cardio:JumpRope" -> row = new Object[]{
                                rs.getInt("sets"),
                                rs.getInt("reps"),
                                rs.getString("intensity"),
                                rs.getDouble("currentHeartRate"),
                                rs.getString("logDT")
                        };

                        case "Strength:Leg", "Strength:Pull", "Strength:Push" -> row = new Object[]{
                                rs.getInt("sets"),
                                rs.getInt("reps"),
                                rs.getString("intensity"),
                                rs.getDouble("weightLifted"),
                                rs.getString("logDT")
                        };

                        default -> row = new Object[]{};
                    }

                    workoutData.add(row);
                }
            }

        } catch (SQLException e) {
            System.err.println("Query error (getWorkoutData): " + e.getMessage());
        }

        return workoutData;
    }

    public double getClobalCalBurn() {
        String sql = "SELECT SUM(caloriesBurned) AS totalCalories FROM activities";
        double totalCalories = 0.0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                totalCalories = rs.getDouble("totalCalories");
            }

        } catch (SQLException e) {
            System.err.println("Query error (tallyGlobalCaloriesBurned): " + e.getMessage());
        }

        return totalCalories;
    }

    public void deleteActivity(int activityID, String deletionTime) {
        String sql = """
        UPDATE activities
        SET durationMinutes = 0,
            caloriesBurned = 0,
            metValue = 0,
            workoutType = 'DELETED',
            startDT = ?  
        WHERE activityID = ?
    """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, deletionTime); // new "creation" timestamp
            pstmt.setInt(2, activityID);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Activity marked as deleted successfully.");
            } else {
                System.out.println("No activity found with the given activityID.");
            }

        } catch (SQLException e) {
            System.err.println("Update failed (deleteActivity): " + e.getMessage());
        }
    }

    public  int getActivityCount() {
        String sql = "SELECT COUNT(*) AS total FROM activities";
        int total = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Query error (countAllActivities): " + e.getMessage());
        }

        return total;
    }

    public Map<String, Integer> countActivitiesByCategory() {
        String sql = """
        SELECT 
            CASE
                WHEN workoutType LIKE 'Cardio:%' THEN 'Cardio'
                WHEN workoutType LIKE 'Strength:%' THEN 'Strength'
                ELSE 'Basic'
            END AS category,
            COUNT(*) AS total
        FROM activities
        WHERE workoutType <> 'DELETED'
        GROUP BY category
    """;

        Map<String, Integer> result = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String category = rs.getString("category");
                int total = rs.getInt("total");
                result.put(category, total);
            }

        } catch (SQLException e) {
            System.err.println("Query error (countActivitiesByCategory): " + e.getMessage());
        }

        return result;
    }
    public int getDeletedActivitiesCount() {
        String sql = "SELECT COUNT(*) AS cnt FROM activities WHERE workoutType = 'DELETED'";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.err.println("Query error (getDeletedActivitiesCount): " + e.getMessage());
        }

        return 0;
    }

    public int getLocalActivityCount() {
        String sql = """
        SELECT COUNT(*) AS cnt
        FROM activities
        WHERE serverOrigin = ?
    """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Bind your constant here
            pstmt.setString(1, SERVER_ORIGIN);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt");
                }
            }
        } catch (SQLException e) {
            System.err.println("Query error (getLocalActivityCount): " + e.getMessage());
        }
        return 0;
    }



}
