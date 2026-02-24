package com.crs.dao;

import com.crs.entity.RecoveryPlan;
import com.crs.util.DbUtil;
import java.sql.*;
import java.time.LocalDateTime;

public class RecoveryPlanDAO {

    public RecoveryPlan findByStudentCourseAttempt(String studentId, String courseCode, int attemptNo) throws SQLException {
        String sql = "SELECT plan_id, student_id, course_code, attempt_no, recommendation, created_by_user_id, created_at FROM recovery_plans WHERE student_id=? AND course_code=? AND attempt_no=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            ps.setInt(3, attemptNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public long create(RecoveryPlan p) throws SQLException {
        String sql = "INSERT INTO recovery_plans(student_id, course_code, attempt_no, recommendation, created_by_user_id, created_at) VALUES(?,?,?,?,?,NOW())";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getStudentId());
            ps.setString(2, p.getCourseCode());
            ps.setInt(3, p.getAttemptNo());
            ps.setString(4, p.getRecommendation());
            ps.setLong(5, p.getCreatedByUserId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    public void updateRecommendation(long planId, String recommendation) throws SQLException {
        String sql = "UPDATE recovery_plans SET recommendation=? WHERE plan_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, recommendation);
            ps.setLong(2, planId);
            ps.executeUpdate();
        }
    }

    public void delete(long planId) throws SQLException {
        String sql = "DELETE FROM recovery_plans WHERE plan_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, planId);
            ps.executeUpdate();
        }
    }

    private RecoveryPlan map(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime created = (ts == null) ? null : ts.toLocalDateTime();
        return new RecoveryPlan(
                rs.getLong("plan_id"),
                rs.getString("student_id"),
                rs.getString("course_code"),
                rs.getInt("attempt_no"),
                rs.getString("recommendation"),
                rs.getLong("created_by_user_id"),
                created
        );
    }
}

