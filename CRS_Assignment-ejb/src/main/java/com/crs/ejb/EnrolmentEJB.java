package com.crs.ejb;

import com.crs.dao.EnrolmentDAO;
import com.crs.dao.StudentDAO;
import com.crs.ejb.dto.EligibilityResult;
import com.crs.ejb.exception.BusinessException;
import com.crs.entity.Enrolment;
import com.crs.entity.Student;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class EnrolmentEJB {

    private static final Logger LOG = Logger.getLogger(EnrolmentEJB.class.getName());

    @EJB
    private EligibilityEJB eligibilityEJB;

    @EJB
    private NotificationEJB notificationEJB;

    private final EnrolmentDAO enrolmentDAO = new EnrolmentDAO();

    public long createEnrolment(String studentId, String courseCode, long createdByUserId) throws SQLException {
        if (studentId == null || studentId.isBlank()) throw new IllegalArgumentException("studentId is required.");
        if (courseCode == null || courseCode.isBlank()) throw new IllegalArgumentException("courseCode is required.");

        final String cleanStudentId = studentId.trim();
        final String cleanCourseCode = courseCode.trim().toUpperCase();

        int maxAttempt = enrolmentDAO.getMaxAttempt(cleanStudentId, cleanCourseCode); 
        if (maxAttempt >= 3) {
            throw new BusinessException("Course recovery policy: max 3 attempts per course. Current max attempt = " + maxAttempt);
        }

        int nextAttempt = maxAttempt + 1;

        EligibilityResult er = eligibilityEJB.checkStudent(cleanStudentId); 

        if (!er.isEligible()) {
            throw new BusinessException("Not eligible for enrolment: " + String.join("; ", er.getReasons()));
        }

        Enrolment e = new Enrolment();
        e.setStudentId(cleanStudentId);    
        e.setCourseCode(cleanCourseCode); 
        e.setAttemptNo(nextAttempt);
        e.setEligibilityStatus("PASS");
        e.setEnrolmentStatus("PENDING");
        e.setCreatedByUserId(createdByUserId);

        long id = enrolmentDAO.createPending(e);

        LOG.info(() -> "ENROLMENT_CREATED enrolmentId=" + id +
                " studentId=" + cleanStudentId +
                " courseCode=" + cleanCourseCode +
                " attemptNo=" + nextAttempt +
                " createdByUserId=" + createdByUserId);

        if (notificationEJB != null) {
            try {
                Student student = new StudentDAO().findById(cleanStudentId);
                String recipient = (student != null) ? student.getEmail() : null;
                if (recipient != null && !recipient.isBlank()) {
                    notificationEJB.sendMilestoneReminderEmail(
                            recipient,
                            "Enrolment request created: " + cleanStudentId + " -> " + cleanCourseCode,
                            "Your enrolment request is PENDING (Eligibility: PASS)."
                    );
                }
            } catch (Exception ex) {
                LOG.log(Level.WARNING, "Notification failed for enrolmentId=" + id + " (ignored)", ex);
            }
        }

        return id;
    }

    public List<Enrolment> listPending() throws SQLException {
        return enrolmentDAO.listPending();
    }

    public List<Enrolment> listPendingForAdmin() throws SQLException {
        return enrolmentDAO.listPendingForAdmin();
    }

    public List<Enrolment> listMyRequests(long createdByUserId) throws SQLException {
        return enrolmentDAO.listByCreator(createdByUserId);
    }

    public void approve(long enrolmentId, long adminUserId) throws SQLException {
        int updated = enrolmentDAO.approvePending(enrolmentId, adminUserId);
        if (updated == 0) {
            throw new BusinessException("Cannot approve: enrolment not found or already decided.");
        }
        LOG.info(() -> "ENROLMENT_APPROVED enrolmentId=" + enrolmentId + " adminUserId=" + adminUserId);
    }

    public void reject(long enrolmentId, long adminUserId, String reason) throws SQLException {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reject reason is required.");
        }
        int updated = enrolmentDAO.rejectPending(enrolmentId, adminUserId, reason.trim());
        if (updated == 0) {
            throw new BusinessException("Cannot reject: enrolment not found or already decided.");
        }
        LOG.info(() -> "ENROLMENT_REJECTED enrolmentId=" + enrolmentId +
                " adminUserId=" + adminUserId +
                " reasonLength=" + reason.trim().length());
    }
}