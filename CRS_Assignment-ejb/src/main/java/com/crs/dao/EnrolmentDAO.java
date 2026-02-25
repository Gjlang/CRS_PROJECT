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

    // 3.1 — updated to save created_by_user_id
    public long createPending(Enrolment e) throws SQLException {
        String sql = "INSERT INTO enrolments(student_id, course_code, attempt_no, eligibility_status, enrolment_status, created_by_user_id, created_at) " +
                     "VALUES(?,?,?,?,?,?,NOW())";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getStudentId());
            ps.setString(2, e.getCourseCode());
            ps.setInt(3, e.getAttemptNo());
            ps.setString(4, e.getEligibilityStatus());
            ps.setString(5, e.getEnrolmentStatus());
            ps.setLong(6, e.getCreatedByUserId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    // Original listPending() — kept for academic officer use
    public List<Enrolment> listPending() throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, " +
                     "created_by_user_id, decided_by_user_id, decided_at, reject_reason, created_at " +
                     "FROM enrolments WHERE enrolment_status='PENDING' ORDER BY created_at DESC";
        List<Enrolment> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    // 3.2 — list pending for admin (separate method)
    public List<Enrolment> listPendingForAdmin() throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, " +
                     "created_by_user_id, decided_by_user_id, decided_at, reject_reason, created_at " +
                     "FROM enrolments WHERE enrolment_status='PENDING' ORDER BY created_at DESC";
        List<Enrolment> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    // 3.3 — list by creator ("My Requests")
    public List<Enrolment> listByCreator(long createdByUserId) throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, " +
                     "created_by_user_id, decided_by_user_id, decided_at, reject_reason, created_at " +
                     "FROM enrolments WHERE created_by_user_id=? ORDER BY created_at DESC";
        List<Enrolment> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, createdByUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    // 3.4 — atomic approve (returns 1 = success, 0 = already decided / not found)
    public int approvePending(long enrolmentId, long adminUserId) throws SQLException {
        String sql = "UPDATE enrolments SET enrolment_status='APPROVED', decided_by_user_id=?, decided_at=NOW(), " +
                     "reject_reason=NULL " +
                     "WHERE enrolment_id=? AND enrolment_status='PENDING'";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, adminUserId);
            ps.setLong(2, enrolmentId);
            return ps.executeUpdate();
        }
    }

    // 3.4 — atomic reject with reason (returns 1 = success, 0 = already decided / not found)
    public int rejectPending(long enrolmentId, long adminUserId, String reason) throws SQLException {
        String sql = "UPDATE enrolments SET enrolment_status='REJECTED', decided_by_user_id=?, decided_at=NOW(), " +
                     "reject_reason=? " +
                     "WHERE enrolment_id=? AND enrolment_status='PENDING'";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, adminUserId);
            ps.setString(2, reason);
            ps.setLong(3, enrolmentId);
            return ps.executeUpdate();
        }
    }

    public Enrolment findApprovedById(long enrolmentId) throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, " +
                     "created_by_user_id, decided_by_user_id, decided_at, reject_reason, created_at " +
                     "FROM enrolments WHERE enrolment_id=? AND enrolment_status='APPROVED'";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, enrolmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
                return null;
            }
        }
    }
    @Deprecated
    public void approve(long enrolmentId) throws SQLException {
        approvePending(enrolmentId, 0L);
    }
    /** @deprecated Use rejectPending(enrolmentId, adminUserId, reason) instead */
    @Deprecated
    public void reject(long enrolmentId) throws SQLException {
        rejectPending(enrolmentId, 0L, null);
    }

    // 3.5 — updated map() to include new fields
    private Enrolment map(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime created = (ts == null) ? null : ts.toLocalDateTime();

        Enrolment e = new Enrolment(
                rs.getLong("enrolment_id"),
                rs.getString("student_id"),
                rs.getString("course_code"),
                rs.getInt("attempt_no"),
                rs.getString("eligibility_status"),
                rs.getString("enrolment_status"),
                created
        );

        e.setCreatedByUserId((Long) rs.getObject("created_by_user_id"));
        e.setDecidedByUserId((Long) rs.getObject("decided_by_user_id"));

        Timestamp dts = rs.getTimestamp("decided_at");
        e.setDecidedAt(dts == null ? null : dts.toLocalDateTime());

        e.setRejectReason(rs.getString("reject_reason"));

        return e;
    }
}