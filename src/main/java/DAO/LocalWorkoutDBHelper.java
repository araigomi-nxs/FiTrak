package DAO;


import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalWorkoutDBHelper {
    private final String DB_URL = "jdbc:sqlite:FitrakAccount.db";
    private final static String SERVER_ORIGIN = Config.get("SERVER_ORIGIN");
    public LocalWorkoutDBHelper() {

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
                CREATE TABLE IF NOT EXISTS workouts (
                workID INTEGER PRIMARY KEY AUTOINCREMENT, 
                activityID INTEGER NOT NULL, 
                steps INTEGER NOT NULL,
                distanceKM REAL,
                intensity TEXT NOT NULL,
                calPerStep REAL,
                speedKPH REAL,
                sets INTEGER NOT NULL,
                reps INTEGER NOT NULL,
                currentHeartRate REAL,
                weightLifted REAL,
                serverOrigin TEXT,
                logDT TEXT NOT NULL,
                FOREIGN KEY (activityID) REFERENCES accounts(userID)
                );
                
                """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
        }
    }


    public void insertWorkout(long activityID, int steps, double distanceKM, String intensity,
                              double calPerStep, double speedKPH, int sets, int reps,
                              double currentHeartRate, double weightLifted, String serverOrigin, String logDT)
    {
        String sql = "INSERT INTO workouts (activityID, steps, distanceKM, intensity, calPerStep, speedKPH, sets, reps, currentHeartRate, weightLifted, serverOrigin, logDT) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

        long workID = -1; // default if insert fails

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, activityID);
            pstmt.setInt(2, steps);
            pstmt.setDouble(3, distanceKM);
            pstmt.setString(4, intensity);
            pstmt.setDouble(5, calPerStep);
            pstmt.setDouble(6, speedKPH);
            pstmt.setInt(7, sets);
            pstmt.setInt(8, reps);
            pstmt.setDouble(9, currentHeartRate);
            pstmt.setDouble(10, weightLifted);
            pstmt.setString(11, serverOrigin);
            pstmt.setString(12, logDT);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        workID = rs.getLong(1); // retrieve auto-generated workID
                    }
                }
            }

            System.out.println("Workout inserted successfully with ID: " + workID);

        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        }

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

    public DefaultTableModel getWorkoutsTableModelLocal() {
        // Match your SQLite schema column names
        String[] columnNames = {
                "WorkID", "ActivityID", "Steps", "DistanceKM", "Intensity",
                "CalPerStep", "SpeedKPH", "Sets", "Reps",
                "CurrentHeartRate", "WeightLifted",
                "ServerOrigin", "LogDT"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT workID, activityID, steps, distanceKM, intensity, calPerStep, " +
                "speedKPH, sets, reps, currentHeartRate, weightLifted, serverOrigin, logDT " +
                "FROM workouts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

            System.out.println("Local workouts table successfully loaded from SQLite.");
        } catch (SQLException e) {
            System.err.println("Local workouts fetch failed: " + e.getMessage());
        }

        return model;
    }

    public int getDeletedLinkedWorkoutCount() {
        String sql = """
        SELECT COUNT(*) AS cnt
        FROM workouts w
        JOIN activities a ON w.activityID = a.activityID
        WHERE a.workoutType = 'DELETED'
    """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.err.println("Query error (getDeletedLinkedWorkoutCount): " + e.getMessage());
        }

        return 0;
    }

    public int getLocalCount() {
        String sql = """
        SELECT COUNT(*) AS cnt
        FROM workouts
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
            System.err.println("Query error (getLocalCount): " + e.getMessage());
        }

        return 0;
    }



}
