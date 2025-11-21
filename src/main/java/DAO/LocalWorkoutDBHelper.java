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

}
