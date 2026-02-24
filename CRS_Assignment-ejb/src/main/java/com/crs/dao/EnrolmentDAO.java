package com.crs.dao;

import com.crs.entity.Enrolment;
import com.crs.util.DbUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EnrolmentDAO {

    public int getMaxAttempt(String studentId, String courseCode) throws SQLException {
        String sql = "SELECT COALESCE(MAX(attempt_no),0) AS max_attempt FROM enrolments WHERE student_id=? AND course_code=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("max_attempt") : 0;
            }
        }
    }

    public long createPending(Enrolment e) throws SQLException {
        String sql = "INSERT INTO enrolments(student_id, course_code, attempt_no, eligibility_status, enrolment_status, created_at) VALUES(?,?,?,?,?,NOW())";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getStudentId());
            ps.setString(2, e.getCourseCode());
            ps.setInt(3, e.getAttemptNo());
            ps.setString(4, e.getEligibilityStatus());
            ps.setString(5, e.getEnrolmentStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    public List<Enrolment> listPending() throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, created_at FROM enrolments WHERE enrolment_status='PENDING' ORDER BY created_at DESC";
        List<Enrolment> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void approve(long enrolmentId) throws SQLException {
        setStatus(enrolmentId, "APPROVED");
    }

    public void reject(long enrolmentId) throws SQLException {
        setStatus(enrolmentId, "REJECTED");
    }

    private void setStatus(long enrolmentId, String status) throws SQLException {
        String sql = "UPDATE enrolments SET enrolment_status=? WHERE enrolment_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, enrolmentId);
            ps.executeUpdate();
        }
    }

    private Enrolment map(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime created = (ts == null) ? null : ts.toLocalDateTime();
        return new Enrolment(
                rs.getLong("enrolment_id"),
                rs.getString("student_id"),
                rs.getString("course_code"),
                rs.getInt("attempt_no"),
                rs.getString("eligibility_status"),
                rs.getString("enrolment_status"),
                created
        );
    }
}

