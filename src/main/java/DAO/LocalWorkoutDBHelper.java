package DAO;


import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class LocalWorkoutDBHelper {
    private final String DB_URL = "jdbc:sqlite:FitrakAccount.db";

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
                              double currentHeartRate, double weightLifted, String serverOrigin, String logDT) {
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



}
