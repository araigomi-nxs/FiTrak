package DAO;

import java.util.UUID;
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
                        rs.getDouble("weightLoss"),
                        rs.getString("serverOrigin")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            System.err.println("Data fetch failed: " + e.getMessage());
        }

        return model;
    }




}
