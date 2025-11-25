package DAO;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class OnlineDataBaseHelper {


    private static final String SDB_URL = "jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres?sslmode=require";

    protected static final String SUPABASE_USER = "postgres.ulasdclgwpkahcyifjqr"; // from Supabase dashboard
    protected static final String SUPABASE_PASSWORD = Config.get("SUPABASE_PASSWORD"); // your actual password
    private static HikariDataSource dataSource;


    protected Connection getConnection() throws SQLException {
       // return DriverManager.getConnection(SDB_URL, SUPABASE_USER, SUPABASE_PASSWORD);
        return dataSource.getConnection();
    }

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres?sslmode=require");
        config.setUsername("postgres.ulasdclgwpkahcyifjqr"); // your Supabase user
        config.setPassword(Config.get("SUPABASE_PASSWORD")); // your password

        // Pool tuning
        config.setMaximumPoolSize(10);       // number of concurrent connections
        config.setMinimumIdle(2);            // keep a couple idle
        config.setIdleTimeout(30000);        // 30s before releasing idle
        config.setConnectionTimeout(10000);  // 10s wait for a connection
        config.setLeakDetectionThreshold(2000); // helps debug leaks

        dataSource = new HikariDataSource(config);
    }





    public DefaultTableModel getAccountsTableModelOnline() {
        // Match Supabase schema column names
        String[] columnNames = {
                "UserID", "Email", "Password", "Privilege", "Username",
                "Sex", "Age", "Weight", "Height", "BMI",
                "ServerOrigin", "Preference", "CreationDT", "LastUpdatedDT"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT user_id, email, password, privilege, username, sex, age, weight, height, bmi, server_origin, preference, creation_dt, last_updated_dt FROM accounts";

        try (Connection conn = getConnection();   // Supabase connection
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getLong("user_id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("privilege"),
                        rs.getString("username"),
                        rs.getString("sex"),
                        rs.getInt("age"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("bmi"),
                        rs.getString("server_origin"),
                        rs.getInt("preference"),
                        rs.getString("creation_dt"),
                        rs.getString("last_updated_dt")
                };
                model.addRow(row);
            }
            System.out.println("Supa base table successfully loaded");

        } catch (SQLException e) {
            System.err.println("Supabase data fetch failed: " + e.getMessage());
        }

        return model;
    }
}

