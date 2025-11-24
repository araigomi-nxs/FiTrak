package DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config/config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties from /config", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
