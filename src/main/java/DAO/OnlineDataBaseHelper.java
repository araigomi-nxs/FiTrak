package DAO;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

}

