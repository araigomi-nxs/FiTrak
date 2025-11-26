package DAO;

import DAO.test.Json;
import DAO.test.SupabaseHttpClient;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
import okhttp3.Response;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class OnlineDataBaseHelper {
    private static HikariDataSource dataSource;
    private final String DB_URL = Config.get("SQLITE_DBURL");
    private final SupabaseHttpClient http;

    public OnlineDataBaseHelper()  {
        try {
            this.http = new SupabaseHttpClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() throws SQLException {
       // return DriverManager.getConnection(SDB_URL, SUPABASE_USER, SUPABASE_PASSWORD);
        //String url = "jdbc:postgresql://ulasdclgwpkahcyifjqr.session-pooler.supabase.com:5432/postgres?sslmode=require";
       // String user = "my_other_user.ulasdclgwpkahcyifjqr";
        //String password = "StrongPasswordHere";
        return dataSource.getConnection();
        //return DriverManager.getConnection(url, user, password);

    }

    static {
        HikariConfig config1 = new HikariConfig();
        config1.setJdbcUrl("jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres?sslmode=require");
        config1.setUsername("postgres.ulasdclgwpkahcyifjqr");
        config1.setPassword("FiTrakApp123");


        HikariConfig config2 = new HikariConfig();
        //config2.setJdbcUrl("jdbc:postgresql://db.ulasdclgwpkahcyifjqr.supabase.co:5432/postgres");
        config2.setJdbcUrl("jdbc:postgresql://aws-1-ap-southeast-2.pooler.supabase.com:5432/postgres?sslmode=require");
        config2.setUsername("my_other_user");
        config2.setPassword("StrongPasswordHere");

        // Pool tuning
        config1.setMaximumPoolSize(10);       // number of concurrent connections
        config1.setMinimumIdle(2);            // keep a couple idle
        config1.setIdleTimeout(30000);        // 30s before releasing idle
        config1.setConnectionTimeout(10000);  // 10s wait for a connection
        config1.setLeakDetectionThreshold(2000); // helps debug leaks

        dataSource = new HikariDataSource(config1);
    }



    public DefaultTableModel getAccountsTableModelOnline() {
        // Match Supabase schema column names
        String[] columnNames = {
                "UserID", "Email", "Password", "Privilege", "Username",
                "Sex", "Age", "Weight", "Height", "BMI",
                "ServerOrigin", "Preference", "CreationDT", "LastUpdatedDT"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (Response resp = http.get("/rest/v1/accounts?select=*")) {
            if (!resp.isSuccessful()) {
                String err = resp.body() != null ? resp.body().string() : "";
                throw new RuntimeException("Supabase data fetch failed: " + resp.code() + " " + err);
            }

            String body = resp.body().string();
            List<Map<String,Object>> accounts = Json.fromJsonList(body);

            for (Map<String,Object> acc : accounts) {
                Object[] row = {
                        acc.get("user_id"),
                        acc.get("email"),
                        acc.get("password"),
                        acc.get("privilege"),
                        acc.get("username"),
                        acc.get("sex"),
                        acc.get("age"),
                        acc.get("weight"),
                        acc.get("height"),
                        acc.get("bmi"),
                        acc.get("server_origin"),
                        acc.get("preference"),
                        acc.get("creation_dt"),
                        acc.get("last_updated_dt")
                };
                model.addRow(row);
            }

            System.out.println("Supabase accounts table successfully loaded via HTTP");
        } catch (Exception e) {
            System.err.println("Supabase data fetch failed: " + e.getMessage());
        }

        return model;
    }


    public DefaultTableModel getActivitiesTableModelOnline() {
        // Match Supabase schema column names
        String[] columnNames = {
                "ActivityID", "UserID", "DurationMinutes", "CaloriesBurned",
                "StartDT", "EndDT", "MetValue", "InitialWeight",
                "WorkoutType", "ServerOrigin"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (Response resp = http.get("/rest/v1/activities?select=*")) {
            if (!resp.isSuccessful()) {
                String err = resp.body() != null ? resp.body().string() : "";
                throw new RuntimeException("Supabase data fetch failed: " + resp.code() + " " + err);
            }

            String body = resp.body().string();
            List<Map<String,Object>> activities = Json.fromJsonList(body);

            for (Map<String,Object> act : activities) {
                Object[] row = {
                        act.get("activity_id"),
                        act.get("user_id"),
                        act.get("duration_minutes"),
                        act.get("calories_burned"),
                        act.get("start_dt"),
                        act.get("end_dt"),
                        act.get("met_value"),
                        act.get("initial_weight"),
                        act.get("workout_type"),
                        act.get("server_origin")
                };
                model.addRow(row);
            }

            System.out.println("Supabase activities table successfully loaded via HTTP");
        } catch (Exception e) {
            System.err.println("Supabase activities fetch failed: " + e.getMessage());
        }

        return model;
    }

    public DefaultTableModel getWorkoutsTableModelOnline() {
        // Match Supabase schema column names
        String[] columnNames = {
                "WorkID", "ActivityID", "Steps", "DistanceKM", "Intensity",
                "CalPerStep", "SpeedKPH", "Sets", "Reps",
                "CurrentHeartRate", "WeightLifted",
                "ServerOrigin", "LogDT"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (Response resp = http.get("/rest/v1/workouts?select=*")) {
            if (!resp.isSuccessful()) {
                String err = resp.body() != null ? resp.body().string() : "";
                throw new RuntimeException("Supabase workouts fetch failed: " + resp.code() + " " + err);
            }

            String body = resp.body().string();
            List<Map<String,Object>> workouts = Json.fromJsonList(body);

            for (Map<String,Object> w : workouts) {
                Object[] row = {
                        w.get("work_id"),
                        w.get("activity_id"),
                        w.get("steps"),
                        w.get("distance_km"),
                        w.get("intensity"),
                        w.get("cal_per_step"),
                        w.get("speed_kph"),
                        w.get("sets"),
                        w.get("reps"),
                        w.get("current_heart_rate"),
                        w.get("weight_lifted"),
                        w.get("server_origin"),
                        w.get("log_dt")
                };
                model.addRow(row);
            }

            System.out.println("Supabase workouts table successfully loaded via HTTP");
        } catch (Exception e) {
            System.err.println("Supabase workouts fetch failed: " + e.getMessage());
        }

        return model;
    }

    public int getWorkoutsRowCountOnline() {
        DefaultTableModel model = getWorkoutsTableModelOnline();
        return model.getRowCount();
    }

    public int getActivitiesRowCountOnline() {
        DefaultTableModel model = getActivitiesTableModelOnline();
        return model.getRowCount();
    }

    public int getAccountsRowCountOnline() {
        DefaultTableModel model = getAccountsTableModelOnline();
        return model.getRowCount();
    }



}



