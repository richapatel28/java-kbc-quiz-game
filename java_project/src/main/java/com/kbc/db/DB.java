package com.kbc.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
    private static final Properties props = new Properties();

    static {
        try (InputStream is = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is != null) {
                props.load(is);
            } else {
                throw new RuntimeException("db.properties not found in resources");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String pass = props.getProperty("db.password");
        return DriverManager.getConnection(url, user, pass);
    }

    public static void ensureSchema() {
        try (Connection conn = getConnection();
             var st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "username VARCHAR(100) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "highscore INT DEFAULT 0" +
                    ") ENGINE=InnoDB");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to ensure schema", e);
        }
    }
}


