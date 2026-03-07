package com.crs.dao;

import com.crs.entity.RecoveryPlan;
import com.crs.util.DbUtil;

import java.sql.*;
import java.time.LocalDateTime;

public class RecoveryPlanDAO {

    /**
     * STRICT: Find a recovery plan only by enrolment_id.
     * Returns null if not found.
     */
    public RecoveryPlan findByEnrolmentId(long enrolmentId) throws SQLException {
        String sql = "SELECT plan_id, recommendation, created_by_user_id, created_at, enrolment_id\n"
        		+ "FROM recovery_plans\n"
        		+ "WHERE enrolment_id = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, enrolmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /**
     * STRICT: Insert a new recovery plan tied to an enrolment_id.
     * Throws IllegalStateException if UNIQUE(enrolment_id) is violated (one plan per enrolment).
     */
    public long insert(RecoveryPlan p) throws SQLException {
        String sql = "INSERT INTO recovery_plans (recommendation, created_by_user_id, enrolment_id, created_at)\n"
        		+ "VALUES (?,?,?,NOW())";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getStudentId());
            ps.setString(2, p.getCourseCode());
            ps.setInt(3, p.getAttemptNo());
            ps.setString(4, p.getRecommendation());
            ps.setLong(5, p.getCreatedByUserId());
            if (p.getEnrolmentId() != null) {
                ps.setLong(6, p.getEnrolmentId());
            } else {
                ps.setNull(6, Types.BIGINT);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new SQLException("Insert succeeded but no generated plan_id returned.");
            }
        } catch (SQLIntegrityConstraintViolationException dup) {
            // Catches DB-level UNIQUE(enrolment_id) violation as a safety net
            throw new IllegalStateException("Recovery plan already exists for this enrolment.", dup);
        }
    }

    /**
     * Update recommendation by plan_id.
     */
    public void updateRecommendation(long planId, String recommendation) throws SQLException {
        String sql = "UPDATE recovery_plans SET recommendation = ? WHERE plan_id = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, recommendation);
            ps.setLong(2, planId);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new IllegalStateException("No recovery plan found with plan_id: " + planId);
        }
    }

    /**
     * Update recommendation by enrolment_id (alternative entry point from EJB).
     */
    public void updateRecommendationByEnrolmentId(long enrolmentId, String recommendation) throws SQLException {
        String sql = "UPDATE recovery_plans SET recommendation = ? WHERE enrolment_id = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, recommendation);
            ps.setLong(2, enrolmentId);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new IllegalStateException("No recovery plan found for enrolment_id: " + enrolmentId);
        }
    }

    /**
     * Delete a plan by plan_id.
     */
    public void delete(long planId) throws SQLException {
        String sql = "DELETE FROM recovery_plans WHERE plan_id = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, planId);
            ps.executeUpdate();
        }
    }

    // ─────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────

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
        p.setEnrolmentId((Long) rs.getObject("enrolment_id")); // nullable
        return p;
    }
}