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

    public List<ProgressionRegistration> listByCreator(long userId) throws SQLException {
        List<ProgressionRegistration> list = new ArrayList<>();

        String sql = """
            SELECT registration_id, student_id, cgpa, failed_course_count,
                   eligibility_status, registration_status, created_by_user_id, created_at
            FROM progression_registrations
            WHERE created_by_user_id = ?
            ORDER BY registration_id DESC
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProgressionRegistration r = new ProgressionRegistration();
                    r.setRegistrationId(rs.getLong("registration_id"));
                    r.setStudentId(rs.getString("student_id"));
                    r.setCgpa(rs.getDouble("cgpa"));
                    r.setFailedCourseCount(rs.getInt("failed_course_count"));
                    r.setEligibilityStatus(rs.getString("eligibility_status"));
                    r.setRegistrationStatus(rs.getString("registration_status"));
                    r.setCreatedByUserId(rs.getLong("created_by_user_id"));

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        r.setCreatedAt(ts.toLocalDateTime());
                    }

                    list.add(r);
                }
            }
        }

        return list;
    }

    public List<ProgressionRegistration> findAll() throws SQLException {
        List<ProgressionRegistration> list = new ArrayList<>();

        String sql = """
            SELECT registration_id, student_id, cgpa, failed_course_count,
                   eligibility_status, registration_status, created_by_user_id, created_at
            FROM progression_registrations
            ORDER BY registration_id DESC
            """;

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProgressionRegistration r = new ProgressionRegistration();
                r.setRegistrationId(rs.getLong("registration_id"));
                r.setStudentId(rs.getString("student_id"));
                r.setCgpa(rs.getDouble("cgpa"));
                r.setFailedCourseCount(rs.getInt("failed_course_count"));
                r.setEligibilityStatus(rs.getString("eligibility_status"));
                r.setRegistrationStatus(rs.getString("registration_status"));
                r.setCreatedByUserId(rs.getLong("created_by_user_id"));

                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    r.setCreatedAt(ts.toLocalDateTime());
                }

                list.add(r);
            }
        }

        return list;
    }
}