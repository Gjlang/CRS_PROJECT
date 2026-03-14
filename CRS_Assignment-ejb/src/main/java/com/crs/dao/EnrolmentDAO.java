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

    // ✏️ CHANGED: course_title → c.CourseName AS course_name, JOIN key course_code → CourseID
    public List<Enrolment> listPendingWithCourseTitle() throws SQLException {
        String sql =
            "SELECT e.enrolment_id, e.student_id, e.course_code, c.CourseName AS course_name, e.attempt_no, " +
            "e.eligibility_status, e.enrolment_status, e.created_by_user_id, e.decided_by_user_id, " +
            "e.decided_at, e.reject_reason, e.created_at " +
            "FROM enrolments e " +
            "JOIN courses c ON c.CourseID = e.course_code " +
            "WHERE e.enrolment_status = 'PENDING' " +
            "ORDER BY e.created_at DESC";

        List<Enrolment> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Enrolment e = map(rs);
                e.setCourseTitle(rs.getString("course_name"));
                list.add(e);
            }
        }
        return list;
    }

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
    
    public Enrolment findByStudentCourseAttempt(String studentId, String courseCode, int attemptNo) throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, " +
                     "created_by_user_id, decided_by_user_id, decided_at, reject_reason, created_at " +
                     "FROM enrolments WHERE student_id=? AND course_code=? AND attempt_no=? " +
                     "ORDER BY enrolment_id DESC LIMIT 1";

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

    public Enrolment findById(long enrolmentId) throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, " +
                     "created_by_user_id, decided_by_user_id, decided_at, reject_reason, created_at " +
                     "FROM enrolments WHERE enrolment_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, enrolmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }
    
    public Enrolment findApprovedByStudentCourseAttempt(String studentId, String courseCode, int attemptNo) throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, " +
                     "created_by_user_id, decided_by_user_id, decided_at, reject_reason, created_at " +
                     "FROM enrolments " +
                     "WHERE student_id=? AND course_code=? AND attempt_no=? AND enrolment_status='APPROVED' " +
                     "ORDER BY enrolment_id DESC LIMIT 1";

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

    public Enrolment findApprovedById(long enrolmentId) throws SQLException {
        String sql = "SELECT enrolment_id, student_id, course_code, attempt_no, eligibility_status, enrolment_status, " +
                     "created_by_user_id, decided_by_user_id, decided_at, reject_reason, created_at " +
                     "FROM enrolments WHERE enrolment_id=? AND enrolment_status='APPROVED'";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, enrolmentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    @Deprecated
    public void approve(long enrolmentId) throws SQLException {
        approvePending(enrolmentId, 0L);
    }

    @Deprecated
    public void reject(long enrolmentId) throws SQLException {
        rejectPending(enrolmentId, 0L, null);
    }

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