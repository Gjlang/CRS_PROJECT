package com.crs.dao;

import com.crs.entity.RecoveryPlan;
import com.crs.util.DbUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class RecoveryPlanDAO {

    /**
     * Find a recovery plan by enrolment_id.
     * Join enrolments so we can still populate studentId, courseCode, attemptNo.
     */
    public RecoveryPlan findByEnrolmentId(long enrolmentId) throws SQLException {
        String sql = """
            SELECT
                rp.plan_id,
                rp.recommendation,
                rp.created_by_user_id,
                rp.created_at,
                rp.enrolment_id,
                e.student_id,
                e.course_code,
                e.attempt_no
            FROM recovery_plans rp
            JOIN enrolments e
              ON e.enrolment_id = rp.enrolment_id
            WHERE rp.enrolment_id = ?
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, enrolmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /**
     * Insert a new recovery plan using the REAL table structure.
     */
    public long insert(RecoveryPlan p) throws SQLException {
        String sql = """
            INSERT INTO recovery_plans (recommendation, created_by_user_id, enrolment_id, created_at)
            VALUES (?, ?, ?, NOW())
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getRecommendation());
            ps.setLong(2, p.getCreatedByUserId());

            if (p.getEnrolmentId() != null) {
                ps.setLong(3, p.getEnrolmentId());
            } else {
                ps.setNull(3, Types.BIGINT);
            }

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new SQLException("Insert succeeded but no generated plan_id returned.");
            }
        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new IllegalStateException("Recovery plan already exists for this enrolment.", dup);
        }
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

    public void updateRecommendationByEnrolmentId(long enrolmentId, String recommendation) throws SQLException {
        String sql = "UPDATE recovery_plans SET recommendation = ? WHERE enrolment_id = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, recommendation);
            ps.setLong(2, enrolmentId);
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new IllegalStateException("No recovery plan found for enrolment_id: " + enrolmentId);
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
        LocalDateTime created = (ts == null) ? null : ts.toLocalDateTime();

        RecoveryPlan p = new RecoveryPlan(
                rs.getLong("plan_id"),
                rs.getString("student_id"),
                rs.getString("course_code"),
                rs.getInt("attempt_no"),
                rs.getString("recommendation"),
                rs.getLong("created_by_user_id"),
                created
        );
        p.setEnrolmentId((Long) rs.getObject("enrolment_id"));
        return p;
    }
}