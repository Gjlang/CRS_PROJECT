package com.crs.ejb;
import com.crs.dao.EnrolmentDAO;
import com.crs.ejb.dto.EligibilityResult;
import com.crs.entity.Enrolment;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;
@Stateless
public class EnrolmentEJB {
    @EJB
    private EligibilityEJB eligibilityEJB;
    @EJB
    private NotificationEJB notificationEJB;

    // 4.1 — createdByUserId added to signature
    public long createEnrolment(String studentId, String courseCode, long createdByUserId) throws SQLException {
        if (studentId == null || studentId.isBlank()) throw new IllegalArgumentException("studentId is required.");
        if (courseCode == null || courseCode.isBlank()) throw new IllegalArgumentException("courseCode is required.");
        studentId = studentId.trim();
        courseCode = courseCode.trim().toUpperCase();
        EnrolmentDAO dao = new EnrolmentDAO();
        int maxAttempt = dao.getMaxAttempt(studentId, courseCode);
        if (maxAttempt >= 3) {
            throw new IllegalStateException("Course retrieval policy: max 3 attempts per course. Current max attempt = " + maxAttempt);
        }
        int nextAttempt = maxAttempt + 1;
        EligibilityResult er = eligibilityEJB.checkStudent(studentId);

        // 4.2 — strict eligibility enforcement: block if FAIL
        if (!er.isEligible()) {
            throw new IllegalStateException("Not eligible for enrolment: " + String.join("; ", er.getReasons()));
        }

        Enrolment e = new Enrolment();
        e.setStudentId(studentId);
        e.setCourseCode(courseCode);
        e.setAttemptNo(nextAttempt);
        e.setEligibilityStatus("PASS"); // always PASS here since FAIL is blocked above
        e.setEnrolmentStatus("PENDING");

        // 4.3 — set createdByUserId into entity before insert
        e.setCreatedByUserId(createdByUserId);

        long id = dao.createPending(e);
        if (notificationEJB != null) {
            notificationEJB.sendMilestoneReminderEmail(
                "student@example.com",
                "Enrolment request created: " + studentId + " -> " + courseCode,
                "Your enrolment request is " + e.getEnrolmentStatus() + " (Eligibility: " + e.getEligibilityStatus() + ").");
        }
        return id;
    }

    // Original listPending() — kept for academic officer use
    public List<Enrolment> listPending() throws SQLException {
        return new EnrolmentDAO().listPending();
    }

    // 4.4 — new list methods split for admin vs academic
    public List<Enrolment> listPendingForAdmin() throws SQLException {
        return new EnrolmentDAO().listPendingForAdmin();
    }

    public List<Enrolment> listMyRequests(long createdByUserId) throws SQLException {
        return new EnrolmentDAO().listByCreator(createdByUserId);
    }

    // 4.5 — approve/reject with adminUserId + reason + validation
    public void approve(long enrolmentId, long adminUserId) throws SQLException {
        int updated = new EnrolmentDAO().approvePending(enrolmentId, adminUserId);
        if (updated == 0) throw new IllegalStateException("Cannot approve: enrolment not found or already decided.");
    }

    public void reject(long enrolmentId, long adminUserId, String reason) throws SQLException {
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Reject reason is required.");
        int updated = new EnrolmentDAO().rejectPending(enrolmentId, adminUserId, reason.trim());
        if (updated == 0) throw new IllegalStateException("Cannot reject: enrolment not found or already decided.");
    }
}