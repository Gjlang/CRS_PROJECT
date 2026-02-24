package com.crs.dao;

import com.crs.entity.Notification;
import com.crs.util.DbUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public long logQueued(Notification n) throws SQLException {
        String sql = "INSERT INTO notifications(recipient_email, subject, body, type, status, created_at) VALUES(?,?,?,?, 'QUEUED', NOW())";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, n.getRecipientEmail());
            ps.setString(2, n.getSubject());
            ps.setString(3, n.getBody());
            ps.setString(4, n.getType());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    public void markSent(long notificationId) throws SQLException {
        String sql = "UPDATE notifications SET status='SENT', error_message=NULL WHERE notification_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, notificationId);
            ps.executeUpdate();
        }
    }

    public void markFailed(long notificationId, String errorMessage) throws SQLException {
        String sql = "UPDATE notifications SET status='FAILED', error_message=? WHERE notification_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, errorMessage);
            ps.setLong(2, notificationId);
            ps.executeUpdate();
        }
    }

    public List<Notification> listHistory(int limit) throws SQLException {
        String sql = "SELECT notification_id, recipient_email, subject, body, type, status, created_at, error_message FROM notifications ORDER BY created_at DESC LIMIT ?";
        List<Notification> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    private Notification map(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime created = (ts == null) ? null : ts.toLocalDateTime();
        return new Notification(
                rs.getLong("notification_id"),
                rs.getString("recipient_email"),
                rs.getString("subject"),
                rs.getString("body"),
                rs.getString("type"),
                rs.getString("status"),
                created,
                rs.getString("error_message")
        );
    }
}

