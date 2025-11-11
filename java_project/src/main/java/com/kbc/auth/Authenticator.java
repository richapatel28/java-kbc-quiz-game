package com.kbc.auth;

import com.kbc.db.DB;
import com.kbc.model.User;

import java.sql.*;

public class Authenticator {
    public Authenticator() {
        DB.ensureSchema();
    }

    public User login(String username, String password) {
        String sql = "SELECT username, highscore FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(rs.getString("username"));
                    user.setHighScore(rs.getInt("highscore"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean signup(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            return false;
        }
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void updateHighScore(String username, int score) {
        String sql = "UPDATE users SET highscore = GREATEST(highscore, ?) WHERE username = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, score);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public record UserScore(String username, int highscore) {}

    public java.util.List<UserScore> getTopUsers(int limit) {
        String sql = "SELECT username, highscore FROM users ORDER BY highscore DESC, username ASC LIMIT ?";
        java.util.ArrayList<UserScore> list = new java.util.ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new UserScore(rs.getString(1), rs.getInt(2)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}


