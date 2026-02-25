package com.crs.dao;
import com.crs.entity.RecoveryPlan;
import com.crs.util.DbUtil;
import java.sql.*;
import java.time.LocalDateTime;
public class RecoveryPlanDAO {

    // 3.2 — new: find plan by enrolment_id
    public RecoveryPlan findByEnrolmentId(long enrolmentId) throws SQLException {
        String sql = "SELECT plan_id, student_id, course_code, attempt_no, recommendation, " +
                     "created_by_user_id, created_at, enrolment_id " +
                     "FROM recovery_plans WHERE enrolment_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, enrolmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    // Original — find by student + course + attempt
    public RecoveryPlan findByStudentCourseAttempt(String studentId, String courseCode, int attemptNo) throws SQLException {
        String sql = "SELECT plan_id, student_id, course_code, attempt_no, recommendation, " +
                     "created_by_user_id, created_at, enrolment_id " +
                     "FROM recovery_plans WHERE student_id=? AND course_code=? AND attempt_no=?";
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

    // 3.2 — new insert() replaces create(), includes enrolment_id
    public long insert(RecoveryPlan p) throws SQLException {
        String sql = "INSERT INTO recovery_plans(student_id, course_code, attempt_no, recommendation, " +
                     "created_by_user_id, enrolment_id, created_at) VALUES(?,?,?,?,?,?,NOW())";
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
                throw new SQLException("Failed to get generated plan_id");
            }
        }
    }

    // Original create() — kept for backward compatibility, delegates to insert()
    /** @deprecated Use insert(RecoveryPlan) instead */
    @Deprecated
    public long create(RecoveryPlan p) throws SQLException {
        return insert(p);
    }

    // Shared — updateRecommendation (unchanged)
    public void updateRecommendation(long planId, String recommendation) throws SQLException {
        String sql = "UPDATE recovery_plans SET recommendation=? WHERE plan_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, recommendation);
            ps.setLong(2, planId);
            ps.executeUpdate();
        }
    }

    // Original — delete (unchanged)
    public void delete(long planId) throws SQLException {
        String sql = "DELETE FROM recovery_plans WHERE plan_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, planId);
            ps.executeUpdate();
        }
    }

    // 3.2 — updated map() to include enrolment_id
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