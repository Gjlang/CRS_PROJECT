package com.crs.ejb;

import com.crs.dao.EnrolmentDAO;
import com.crs.dao.StudentDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.ejb.dto.EligibilityResult;
import com.crs.ejb.exception.BusinessException;
import com.crs.entity.Enrolment;
import com.crs.entity.Student;
import com.crs.entity.StudentResult;

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
    private final StudentResultDAO studentResultDAO = new StudentResultDAO();

    /**
     * Progression / next-level registration flow
     */
    public long createProgressionEnrolment(String studentId, String courseCode, int attemptNo, long createdByUserId) throws SQLException {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID is required.");
        }
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Course code is required.");
        }
        if (attemptNo <= 0 || attemptNo > 3) {
            throw new IllegalArgumentException("Attempt No must be between 1 and 3.");
        }

        final String cleanStudentId = studentId.trim().toUpperCase();
        final String cleanCourseCode = courseCode.trim().toUpperCase();

        StudentDAO studentDAO = new StudentDAO();
        Student student = studentDAO.findById(cleanStudentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + cleanStudentId);
        }

        EligibilityResult er = eligibilityEJB.checkStudent(cleanStudentId);
        String eligibilityStatus = er.isEligible() ? "PASS" : "FAIL";

        Enrolment existing = enrolmentDAO.findByStudentCourseAttempt(cleanStudentId, cleanCourseCode, attemptNo);
        if (existing != null) {
            throw new BusinessException(
                    "An enrolment record already exists for "
                            + cleanStudentId + " / " + cleanCourseCode + " / attempt " + attemptNo + "."
            );
        }

        Enrolment e = new Enrolment();
        e.setStudentId(cleanStudentId);
        e.setCourseCode(cleanCourseCode);
        e.setAttemptNo(attemptNo);
        e.setEligibilityStatus(eligibilityStatus);
        e.setEnrolmentStatus(er.isEligible() ? "APPROVED" : "PENDING");
        e.setCreatedByUserId(createdByUserId);

        long id = enrolmentDAO.createPending(e);

        LOG.info(() -> "PROGRESSION_ENROLMENT_CREATED enrolmentId=" + id +
                " studentId=" + cleanStudentId +
                " courseCode=" + cleanCourseCode +
                " attemptNo=" + attemptNo +
                " eligibilityStatus=" + eligibilityStatus);

        return id;
    }

    /**
     * Recovery enrolment flow (keep for compatibility if other modules still call it)
     */
    public long createEnrolment(String studentId, String courseCode, int attemptNo, long createdByUserId) throws SQLException {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("studentId is required.");
        }
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("courseCode is required.");
        }
        if (attemptNo <= 0 || attemptNo > 3) {
            throw new BusinessException("Attempt number must be between 1 and 3.");
        }

        final String cleanStudentId = studentId.trim().toUpperCase();
        final String cleanCourseCode = courseCode.trim().toUpperCase();

        List<StudentResult> failedComponents =
                studentResultDAO.findFailedComponents(cleanStudentId, cleanCourseCode, attemptNo);

        if (failedComponents == null || failedComponents.isEmpty()) {
            throw new BusinessException(
                    "Cannot create recovery enrolment: no failed components found for "
                            + cleanStudentId + " / " + cleanCourseCode + " / attempt " + attemptNo + "."
            );
        }

        Enrolment existing = enrolmentDAO.findByStudentCourseAttempt(cleanStudentId, cleanCourseCode, attemptNo);
        if (existing != null) {
            throw new BusinessException(
                    "A recovery enrolment request already exists for "
                            + cleanStudentId + " / " + cleanCourseCode + " / attempt " + attemptNo + "."
            );
        }

        EligibilityResult er = eligibilityEJB.checkStudent(cleanStudentId);
        String eligibilityStatus = er.isEligible() ? "PASS" : "FAIL";

        Enrolment e = new Enrolment();
        e.setStudentId(cleanStudentId);
        e.setCourseCode(cleanCourseCode);
        e.setAttemptNo(attemptNo);
        e.setEligibilityStatus(eligibilityStatus);
        e.setEnrolmentStatus("PENDING");
        e.setCreatedByUserId(createdByUserId);

        long id = enrolmentDAO.createPending(e);

        if (notificationEJB != null) {
            try {
                Student student = new StudentDAO().findById(cleanStudentId);
                String recipient = (student != null) ? student.getEmail() : null;
                if (recipient != null && !recipient.isBlank()) {
                    notificationEJB.sendMilestoneReminderEmail(
                            recipient,
                            "Recovery enrolment request created: " + cleanStudentId + " -> " + cleanCourseCode,
                            "Your recovery enrolment request is PENDING. Eligibility status: " + eligibilityStatus
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

    public List<Enrolment> listAll() throws SQLException {
        return enrolmentDAO.listByCreator(0L); // fallback, change later if you add DAO.findAll()
    }

    public void approve(long enrolmentId, long adminUserId) throws SQLException {
        int updated = enrolmentDAO.approvePending(enrolmentId, adminUserId);
        if (updated == 0) {
            throw new BusinessException("Cannot approve: enrolment not found or already decided.");
        }
    }

    public void reject(long enrolmentId, long adminUserId, String reason) throws SQLException {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reject reason is required.");
        }
        int updated = enrolmentDAO.rejectPending(enrolmentId, adminUserId, reason.trim());
        if (updated == 0) {
            throw new BusinessException("Cannot reject: enrolment not found or already decided.");
        }
    }
}
