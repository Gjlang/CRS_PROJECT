package com.crs.dao;

import com.crs.entity.User;
import com.crs.util.DbUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User findByEmail(String email) throws SQLException {
        String sql = """
            SELECT user_id, full_name, email, password_hash, role, active, created_at, reset_token, reset_token_expiry
            FROM users
            WHERE email=?
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public User findById(long id) throws SQLException {
        String sql = """
            SELECT user_id, full_name, email, password_hash, role, active, created_at, reset_token, reset_token_expiry
            FROM users
            WHERE user_id=?
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public User findByResetToken(String token) throws SQLException {
        String sql = """
            SELECT user_id, full_name, email, password_hash, role, active, created_at, reset_token, reset_token_expiry
            FROM users
            WHERE reset_token = ?
              AND reset_token_expiry IS NOT NULL
              AND reset_token_expiry >= NOW()
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = """
            SELECT user_id, full_name, email, password_hash, role, active, created_at, reset_token, reset_token_expiry
            FROM users
            ORDER BY user_id DESC
            """;
        List<User> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public long create(User u) throws SQLException {
        String sql = """
            INSERT INTO users(full_name,email,password_hash,role,active,created_at)
            VALUES(?,?,?,?,?,NOW())
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getRole());
            ps.setBoolean(5, u.isActive());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    public void update(User u) throws SQLException {
        String sql = """
            UPDATE users
            SET full_name=?, email=?, role=?, active=?
            WHERE user_id=?
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRole());
            ps.setBoolean(4, u.isActive());
            ps.setLong(5, u.getUserId());
            ps.executeUpdate();
        }
    }

    public void deactivate(long userId) throws SQLException {
        String sql = "UPDATE users SET active=0 WHERE user_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    public void updatePasswordHash(long userId, String passwordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash=? WHERE user_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, passwordHash);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public void saveResetToken(long userId, String token) throws SQLException {
        String sql = "UPDATE users " +
                     "SET reset_token=?, reset_token_expiry=DATE_ADD(NOW(), INTERVAL 30 MINUTE) " +
                     "WHERE user_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    public void clearResetToken(long userId) throws SQLException {
        String sql = "UPDATE users SET reset_token=NULL, reset_token_expiry=NULL WHERE user_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    private User map(ResultSet rs) throws SQLException {
        Timestamp createdTs = rs.getTimestamp("created_at");
        Timestamp expiryTs = rs.getTimestamp("reset_token_expiry");

        User u = new User(
                rs.getLong("user_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("role"),
                rs.getBoolean("active"),
                createdTs == null ? null : createdTs.toLocalDateTime()
        );

        u.setResetToken(rs.getString("reset_token"));
        u.setResetTokenExpiry(expiryTs == null ? null : expiryTs.toLocalDateTime());

        return u;
    }
}