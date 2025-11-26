package DAO;

import calculationModels.metrics.MetricsCalculator;
import objects.Account;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDateTime;



public class LocalDataBaseHelper {

    private final String DB_URL = "jdbc:sqlite:FitrakAccount.db";
    private final String SERVER_ORIGIN = Config.get("SERVER_ORIGIN");
    LocalDateTime localDateTime = LocalDateTime.now();


    public LocalDataBaseHelper() {
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
                sex TEXT NOT NULL,
                age INTEGER NOT NULL,
                weight INTEGER NOT NULL,
                height INTEGER NOT NULL,
                BMI INTEGER NOT NULL,
                serverOrigin TEXT NOT NULL,
                preference INTEGER NOT NULL,
                creationDT TEXT NOT NULL,
                lastUpdatedDT TEXT NOT NULL
            );
            """;


        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
        }
    }


    public void insertUser( long userID, String email, String password, int privilege, String creationDT, String lastUpdatedDT) {
        String sql = "INSERT INTO accounts (userID,email, password, privilege, username, sex ,age, weight, height, BMI, serverOrigin,preference,creationDT,lastUpdatedDT) VALUES(?, ?, ?, ?,?, ? , ? , ?,?,?,?,?,?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userID);
            pstmt.setString(2,email );
            pstmt.setString(3, password);
            pstmt.setInt(4, privilege);
            pstmt.setString(5, "anonymous");
            pstmt.setString(6, "none");
            pstmt.setInt(7, 0);
            pstmt.setDouble(8, 0);
            pstmt.setDouble(9, 0);
            pstmt.setDouble(10, 0);
            pstmt.setString(11, SERVER_ORIGIN);
            pstmt.setDouble(12, 0);
            pstmt.setString(13, creationDT);
            pstmt.setString(14,lastUpdatedDT);


            pstmt.executeUpdate();

            System.out.println("User inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        }
    }

    public void insertUser(long userID,String email,String password,int privilege, String username, String sex,int age, double weight,double height, double bmi, String serverOrigin,int preference, String creationDT,String lastUpdatedDT) {
        String sql = "INSERT INTO accounts (userID, email, password, privilege, username, sex, age, weight, height, BMI, serverOrigin, preference, creationDT, lastUpdatedDT) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userID);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setInt(4, privilege);
            pstmt.setString(5, username);
            pstmt.setString(6, sex);
            pstmt.setInt(7, age);
            pstmt.setDouble(8, weight);
            pstmt.setDouble(9, height);
            pstmt.setDouble(10, bmi);
            pstmt.setString(11, serverOrigin);
            pstmt.setInt(12, preference);
            pstmt.setString(13, creationDT);
            pstmt.setString(14, lastUpdatedDT);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User inserted successfully (full data).");
            }

        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        }
    }


    public void updateWH(long userID, String username, double weight, double height, String sex, int age) {
        String sql = "UPDATE accounts SET username = ?, sex =? ,age=?,  weight = ?, height = ?, BMI = ? WHERE userID = ?";

       double bmi = MetricsCalculator.calculateBMI(weight, height);


        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, sex);
            pstmt.setInt(3, age);
            pstmt.setDouble(4, weight);
            pstmt.setDouble(5, height);
            pstmt.setDouble(6, bmi);
            pstmt.setLong(7, userID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User data updated successfully.");
            } else {
                System.out.println("No user found with the given userID.");
            }

        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
        }
    }
    public void updatePref(long userID, int preference) {
        String sql = "UPDATE accounts SET preference = ? WHERE userID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, preference);
            pstmt.setLong(2, userID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User data updated successfully.");
            } else {
                System.out.println("No user found with the given userID.");
            }

        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
        }
    }


    public void updateAll(long userID, String email, String password, int privilege, String username, String sex, int age, double weight, double height, double bmi, String serverOrigin, int preference, String createDT,String lastUpdatedDT ) {
        String sql = "UPDATE accounts SET email = ?,password = ?,privilege = ?,username = ?, sex =? ,age=?,  weight = ?, height = ?, BMI = ? ,serverOrigin = ?,preference =?,creationDT = ? , lastUpdatedDT= ?  WHERE userID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setInt(3, privilege);
            pstmt.setString(4, username);
            pstmt.setString(5, sex);
            pstmt.setInt(6, age);
            pstmt.setDouble(7, weight);
            pstmt.setDouble(8, height);
            pstmt.setDouble(9, bmi);
            pstmt.setString(10, serverOrigin);
            pstmt.setInt(11, preference);
            pstmt.setString(12, createDT);
            pstmt.setString(13, lastUpdatedDT);
            pstmt.setLong(14, userID);


            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User data updated successfully.");
            } else {
                System.out.println("No user found with the given userID.");
            }

        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
        }
    }
    public int checkUserExists(long userID) {
        String sqlMatch = "SELECT 1 FROM accounts WHERE userID = ? LIMIT 1";

        try(Connection conn = DriverManager.getConnection(DB_URL)){
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMatch)) {
                pstmt.setLong(1, userID);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return 1;
                    }
                }
            }

            return 0; // No match at all
        }
        catch (SQLException e) {
            System.err.println("UserID check failed: " + e.getMessage());
            return 0;
        }
    }
    public int checkEmailExists( String email ) {   //sign up
        String sqlMatch = "SELECT 1 FROM accounts WHERE email = ? LIMIT 1";
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMatch)) {
                pstmt.setString(1, email);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return 1;
                    }
                }
            }

            return 0; // No match at all
        }
        catch (SQLException e) {
            System.err.println("Email check failed: " + e.getMessage());
            return 0;
        }
    }
    public int checkUniqueUsername( String username ) {  // sign in
        String sqlMatch = "SELECT 1 FROM accounts WHERE username = ? LIMIT 1";
        try(Connection conn = DriverManager.getConnection(DB_URL)){
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMatch)) {
                pstmt.setString(1, username);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return 1;
                    }
                }
            }
            return 0; // No match at all
        }
        catch (SQLException e) {
            System.err.println("Email check failed: " + e.getMessage());
            return 0;
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
    public void removeUser(long userID, String lastUpdateTime) {
        String sql = """
        UPDATE accounts
        SET password = 'DELETED',
            username = 'DELETED',
            sex = 'DELETED',
            lastUpdatedDT = ?,
            
            privilege = -1,
            age = 0,
            weight = 0,
            height = 0,
            BMI = 0,
            preference = 0
        WHERE userID = ?
    """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, lastUpdateTime);
            pstmt.setLong(2, userID);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User marked as deleted successfully.");
            } else {
                System.out.println("No user found with the given userID.");
            }

        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
        }
    }

    public long getID(String email) {
        String sql = "SELECT userID FROM accounts WHERE email = ?";


        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
              return rs.getLong("userID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        return 0;
    }

    public DefaultTableModel getAccountsTableModel() {
        String[] columnNames = {"ID", "UserID", "Email", "Password", "Privilege", "Username", "Sex","Age","Weight", "Height", "BMI","ServerOrigin", "Preference", "CreationDT", "LastUpdatedDT" };
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
                        rs.getString("sex"),
                        rs.getInt("age"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("BMI"),
                        rs.getString("serverOrigin"),
                        rs.getInt("preference"),
                        rs.getString("creationDT"),
                        rs.getString("lastUpdatedDT")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            System.err.println("Data fetch failed: " + e.getMessage());
        }

        return model;



    }

    public int getRowCount( int mode) {
        int count = 0;
        String sql;

        // Mode selection
        switch (mode) {
            case 0: // All accounts
                sql = "SELECT COUNT(*) FROM  accounts" ;
                break;
            case 1: // Only admin accounts (privilege = 1)
                sql = "SELECT COUNT(*) FROM  accounts WHERE privilege = 1";
                break;
            case 2: // Only non-admin accounts (privilege = 0)
                sql = "SELECT COUNT(*) FROM  accounts WHERE privilege = 0";
                break;
            case 3:
                sql = "SELECT COUNT(*) FROM  accounts WHERE serverOrigin = ?";
                break;
            case 4:
                sql = "SELECT COUNT(*) FROM  accounts WHERE privilege = -1";
                break;

            default:
                throw new IllegalArgumentException("Invalid mode: " + mode);
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Only bind parameter for mode 3
            if (mode == 3) {
                pstmt.setString(1, SERVER_ORIGIN);  // use your Config constant
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Query error (getRowCount): " + e.getMessage());
        }

        return count;
    }


    public Account getAccount(long userID) {
        String sql = "SELECT userID, email, password , privilege, username , weight, height, BMI, age, sex, serverOrigin, preference, creationDT , lastUpdatedDT FROM accounts WHERE userID = ?";
        Account account = null;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                account = new Account(
                        rs.getLong("userID"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("privilege"),
                        rs.getString("username"),
                        rs.getString("creationDT"),
                        rs.getString("lastUpdatedDT"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("BMI"),
                        rs.getInt("age"),
                        rs.getString("sex"),
                        rs.getString("serverOrigin"),
                        rs.getInt("preference")


                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account; // null if not found
    }


    public DefaultTableModel searchAccounts(String keyword, String filter) {
        String[] columnNames = {
                "ID", "UserID", "Email", "Password", "Privilege",
                "Username", "Sex", "Age", "Weight", "Height",
                "BMI", "ServerOrigin", "Preference", "CreationDT", "LastUpdatedDT"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Base query
        String sql = """
            SELECT * FROM accounts
            WHERE (
               email LIKE ? 
               OR username LIKE ? 
               OR serverOrigin LIKE ?
               OR id = ? 
               OR userID = ? 
               OR age = ? 
               OR weight = ? 
               OR height = ? 
               OR BMI = ? 
               OR privilege = ? 
               OR preference = ?
               OR creationDT LIKE ?
               OR lastUpdatedDT LIKE ?
            )
            COLLATE NOCASE
        """;

        // Apply filter
        switch (filter.toLowerCase()) {
            case "latest":
                sql += " ORDER BY creationDT DESC";
                break;
            case "oldest":
                sql += " ORDER BY creationDT ASC";
                break;
            case "male":
                sql += " AND sex = 'male'";
                break;
            case "female":
                sql += " AND sex = 'female'";
                break;
            case "admin":
                sql += " AND privilege = 1";
                break;
            case "user":
                sql += " AND privilege = 0";
                break;
            default:
                // no extra filter
                break;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(12, searchPattern);
            pstmt.setString(13, searchPattern);

            Integer intKeyword = null;
            try {
                intKeyword = Integer.parseInt(keyword);
            } catch (NumberFormatException ignored) {}

            if (intKeyword != null) {
                pstmt.setInt(4, intKeyword);
                pstmt.setInt(5, intKeyword);
                pstmt.setInt(6, intKeyword);
                pstmt.setInt(7, intKeyword);
                pstmt.setInt(8, intKeyword);
                pstmt.setInt(9, intKeyword);
                pstmt.setInt(10, intKeyword);
                pstmt.setInt(11, intKeyword);
            } else {
                for (int i = 4; i <= 11; i++) {
                    pstmt.setInt(i, -1);
                }
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getLong("userID"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("privilege"),
                        rs.getString("username"),
                        rs.getString("sex"),
                        rs.getInt("age"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("BMI"),
                        rs.getString("serverOrigin"),
                        rs.getInt("preference"),
                        rs.getString("creationDT"),
                        rs.getString("lastUpdatedDT")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }

        return tableModel;
    }

    public double getAverages(String column) {
        String sql = "SELECT AVG(BMI) AS avg_bmi, AVG(height) AS avg_height, AVG(weight) AS avg_weight FROM accounts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                double avgBMI = rs.getDouble("avg_bmi");
                double avgHeight = rs.getDouble("avg_height");
                double avgWeight = rs.getDouble("avg_weight");

                return switch (column) {
                    case "bmi" -> avgBMI;
                    case "height" -> avgHeight;
                    case "weight" -> avgWeight;
                    default -> 0.0;
                };
            }
        } catch (SQLException e) {
            System.err.println("Error calculating averages: " + e.getMessage());
        }
        return 0.0; // fallback if query fails
    }






}

