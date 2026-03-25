package com.crs.dao;

import com.crs.entity.RecoveryPlan;
import java.util.ArrayList;
import java.util.List;
import com.crs.util.DbUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class RecoveryPlanDAO {

    public RecoveryPlan findByStudentCourseAttempt(String studentId, String courseCode, int attemptNo) throws SQLException {
        String sql = """
            SELECT
                plan_id,
                student_id,
                course_code,
                attempt_no,
                recommendation,
                created_by_user_id,
                created_at
            FROM recovery_plans
            WHERE student_id = ? AND course_code = ? AND attempt_no = ?
            """;

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

    public long insert(RecoveryPlan p) throws SQLException {
        String sql = """
            INSERT INTO recovery_plans
            (student_id, course_code, attempt_no, recommendation, created_by_user_id, created_at)
            VALUES (?, ?, ?, ?, ?, NOW())
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getStudentId());
            ps.setString(2, p.getCourseCode());
            ps.setInt(3, p.getAttemptNo());
            ps.setString(4, p.getRecommendation());
            ps.setLong(5, p.getCreatedByUserId());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new SQLException("Insert succeeded but no generated plan_id returned.");
            }
        }
    }
    
    public List<RecoveryPlan> findAll() throws SQLException {
        String sql = """
            SELECT plan_id, student_id, course_code, attempt_no, recommendation,
                   created_by_user_id, created_at
            FROM recovery_plans
            ORDER BY plan_id DESC
            """;

        List<RecoveryPlan> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }

    public void updateRecommendation(long planId, String recommendation) throws SQLException {
        String sql = "UPDATE recovery_plans SET recommendation = ? WHERE plan_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, recommendation);
            ps.setLong(2, planId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new IllegalStateException("No recovery plan found with plan_id: " + planId);
            }
        }
    }

    public void delete(long planId) throws SQLException {
        String sql = "DELETE FROM recovery_plans WHERE plan_id = ?";

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, planId);
            ps.executeUpdate();
        }
    }

    private RecoveryPlan map(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = ts != null ? ts.toLocalDateTime() : null;

        RecoveryPlan p = new RecoveryPlan();
        p.setPlanId(rs.getLong("plan_id"));
        p.setStudentId(rs.getString("student_id"));
        p.setCourseCode(rs.getString("course_code"));
        p.setAttemptNo(rs.getInt("attempt_no"));
        p.setRecommendation(rs.getString("recommendation"));
        p.setCreatedByUserId(rs.getLong("created_by_user_id"));
        p.setCreatedAt(createdAt);

        return p;
    }
}