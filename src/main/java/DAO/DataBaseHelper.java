package DAO;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DataBaseHelper {

    private final String DB_URL = "jdbc:sqlite:FitrakAccount.db";

    public DataBaseHelper() {
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
        String sql = """
            
                CREATE TABLE IF NOT EXISTS accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userID INTEGER NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                privilege INTEGER NOT NULL,
                username TEXT NOT NULL,
                weight INTEGER NOT NULL,
                height INTEGER NOT NULL
                
            );  
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
        }
    }

    public void insertUser( long userID, String email, String password, int privilege) {
        String sql = "INSERT INTO accounts (userID,email, password, privilege, username, weight, height) VALUES(?, ?, ?, ?, ? , ? , ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userID);
            pstmt.setString(2,email );
            pstmt.setString(3, password);
            pstmt.setInt(4, privilege);
            pstmt.setString(5, "anonymous");
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);

            pstmt.executeUpdate();

            System.out.println("User inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        }
    }

    public int checkCredentials(String email, String password, int privilege) {
        String sqlFullMatch = "SELECT 1 FROM accounts WHERE email = ? AND password = ? AND privilege = ? LIMIT 1";
        String sqlBasicMatch = "SELECT 1 FROM accounts WHERE email = ? AND password = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // First check full match: email + password + privilege
            try (PreparedStatement pstmt = conn.prepareStatement(sqlFullMatch)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                pstmt.setInt(3, privilege);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return 2; // Full match
                    }
                }
            }

            // Then check basic match: email + password only
            try (PreparedStatement pstmt = conn.prepareStatement(sqlBasicMatch)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return 1; // Email/password match, but privilege mismatch
                    }
                }
            }

            return 0; // No match at all

        } catch (SQLException e) {
            System.err.println("Credential check failed: " + e.getMessage());
            return 0;
        }
    }

    public DefaultTableModel getAccountsTableModel() {
        String[] columnNames = {"ID", "UserID", "Email", "Password", "Privilege", "Username", "Weight", "Height"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT * FROM accounts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getLong("userID"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("privilege"),
                        rs.getString("username"),
                        rs.getInt("weight"),
                        rs.getInt("height")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            System.err.println("Data fetch failed: " + e.getMessage());
        }

        return model;



    }
    }

