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

    public long createEnrolment(String studentId, String courseCode) throws SQLException {
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

        Enrolment e = new Enrolment();
        e.setStudentId(studentId);
        e.setCourseCode(courseCode);
        e.setAttemptNo(nextAttempt);
        e.setEligibilityStatus(er.isEligible() ? "PASS" : "FAIL");
        e.setEnrolmentStatus("PENDING");

        long id = dao.createPending(e);

        if (notificationEJB != null) {
            notificationEJB.sendMilestoneReminderEmail(
                "student@example.com",
                "Enrolment request created: " + studentId + " -> " + courseCode,
                "Your enrolment request is " + e.getEnrolmentStatus() + " (Eligibility: " + e.getEligibilityStatus() + ").");
        }
        return id;
    }

    public List<Enrolment> listPending() throws SQLException {
        return new EnrolmentDAO().listPending();
    }

    public void approve(long enrolmentId) throws SQLException {
        new EnrolmentDAO().approve(enrolmentId);
    }

    public void reject(long enrolmentId) throws SQLException {
        new EnrolmentDAO().reject(enrolmentId);
    }
}

