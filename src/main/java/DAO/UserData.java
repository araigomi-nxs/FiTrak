package DAO;

import org.sqlite.core.DB;

import java.sql.*;
import java.time.LocalDateTime;

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
                "WHERE userID = ? AND date(creationDT) = date('now','localtime')";
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




}
