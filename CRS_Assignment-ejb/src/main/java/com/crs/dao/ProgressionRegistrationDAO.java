package com.crs.dao;

import com.crs.entity.ProgressionRegistration;
import com.crs.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgressionRegistrationDAO {

    public long create(ProgressionRegistration r) throws SQLException {
        String sql = """
            INSERT INTO progression_registrations
            (student_id, cgpa, failed_course_count, eligibility_status, registration_status, created_by_user_id)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getStudentId());
            ps.setDouble(2, r.getCgpa());
            ps.setInt(3, r.getFailedCourseCount());
            ps.setString(4, r.getEligibilityStatus());
            ps.setString(5, r.getRegistrationStatus());
            ps.setLong(6, r.getCreatedByUserId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Failed to create progression registration.");
    }

    public ProgressionRegistration findOpenRequestByStudentId(String studentId) throws SQLException {
        String sql = """
            SELECT registration_id, student_id, cgpa, failed_course_count,
                   eligibility_status, registration_status, created_by_user_id,
                   decided_by_user_id, decided_at, reject_reason, created_at
            FROM progression_registrations
            WHERE student_id = ?
              AND registration_status IN ('PENDING', 'APPROVED')
            ORDER BY registration_id DESC
            LIMIT 1
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }

        return null;
    }

    public int approvePending(long registrationId, long adminUserId) throws SQLException {
        String sql = """
            UPDATE progression_registrations
            SET registration_status = 'APPROVED',
                decided_by_user_id = ?,
                decided_at = NOW(),
                reject_reason = NULL
            WHERE registration_id = ?
              AND registration_status = 'PENDING'
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, adminUserId);
            ps.setLong(2, registrationId);
            return ps.executeUpdate();
        }
    }

    public int rejectPending(long registrationId, long adminUserId, String reason) throws SQLException {
        String sql = """
            UPDATE progression_registrations
            SET registration_status = 'REJECTED',
                decided_by_user_id = ?,
                decided_at = NOW(),
                reject_reason = ?
            WHERE registration_id = ?
              AND registration_status = 'PENDING'
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, adminUserId);
            ps.setString(2, reason);
            ps.setLong(3, registrationId);
            return ps.executeUpdate();
        }
    }

    public List<ProgressionRegistration> listByCreator(long userId) throws SQLException {
        List<ProgressionRegistration> list = new ArrayList<>();

        String sql = """
            SELECT registration_id, student_id, cgpa, failed_course_count,
                   eligibility_status, registration_status, created_by_user_id,
                   decided_by_user_id, decided_at, reject_reason, created_at
            FROM progression_registrations
            WHERE created_by_user_id = ?
            ORDER BY registration_id DESC
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }

        return list;
    }

    public List<ProgressionRegistration> findAll() throws SQLException {
        List<ProgressionRegistration> list = new ArrayList<>();

        String sql = """
            SELECT registration_id, student_id, cgpa, failed_course_count,
                   eligibility_status, registration_status, created_by_user_id,
                   decided_by_user_id, decided_at, reject_reason, created_at
            FROM progression_registrations
            ORDER BY registration_id DESC
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }

    private ProgressionRegistration map(ResultSet rs) throws SQLException {
        ProgressionRegistration r = new ProgressionRegistration();
        r.setRegistrationId(rs.getLong("registration_id"));
        r.setStudentId(rs.getString("student_id"));
        r.setCgpa(rs.getDouble("cgpa"));
        r.setFailedCourseCount(rs.getInt("failed_course_count"));
        r.setEligibilityStatus(rs.getString("eligibility_status"));
        r.setRegistrationStatus(rs.getString("registration_status"));
        r.setCreatedByUserId(rs.getLong("created_by_user_id"));

        Object decidedBy = rs.getObject("decided_by_user_id");
        if (decidedBy != null) {
            r.setDecidedByUserId(((Number) decidedBy).longValue());
        }

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            r.setCreatedAt(createdTs.toLocalDateTime());
        }

        Timestamp decidedTs = rs.getTimestamp("decided_at");
        if (decidedTs != null) {
            r.setDecidedAt(decidedTs.toLocalDateTime());
        }

        r.setRejectReason(rs.getString("reject_reason"));

        return r;
    }
}