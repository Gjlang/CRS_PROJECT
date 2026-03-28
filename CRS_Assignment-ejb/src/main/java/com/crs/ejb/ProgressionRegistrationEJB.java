package com.crs.ejb;

import com.crs.dao.ProgressionRegistrationDAO;
import com.crs.dao.StudentDAO;
import com.crs.entity.ProgressionRegistration;
import com.crs.entity.Student;
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.util.List;

@Stateless
public class ProgressionRegistrationEJB {

    public long createRegistration(String studentId, long createdByUserId) throws SQLException {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("Student ID is required.");
        }

        String cleanStudentId = studentId.trim().toUpperCase();

        StudentDAO studentDAO = new StudentDAO();
        Student student = studentDAO.findById(cleanStudentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + cleanStudentId);
        }

        double cgpa = studentDAO.calculateCgpaOverall(cleanStudentId);
        int failedCourseCount = studentDAO.countFailedCourses(cleanStudentId);

        boolean eligible = (cgpa >= 2.0 && failedCourseCount <= 3);
        if (!eligible) {
            throw new IllegalStateException(
                "Student " + cleanStudentId +
                " is not eligible to progress. CGPA must be at least 2.0 and failed courses must not exceed 3."
            );
        }

        ProgressionRegistrationDAO dao = new ProgressionRegistrationDAO();

        ProgressionRegistration existingOpen = dao.findOpenRequestByStudentId(cleanStudentId);
        if (existingOpen != null) {
            throw new IllegalStateException(
                "An active progression request already exists for student " + cleanStudentId +
                " with status: " + existingOpen.getRegistrationStatus()
            );
        }

        ProgressionRegistration r = new ProgressionRegistration();
        r.setStudentId(cleanStudentId);
        r.setCgpa(cgpa);
        r.setFailedCourseCount(failedCourseCount);
        r.setEligibilityStatus("PASS");
        r.setRegistrationStatus("PENDING");
        r.setCreatedByUserId(createdByUserId);

        return dao.create(r);
    }

    public void approveRegistration(long registrationId, long adminUserId) throws SQLException {
        ProgressionRegistrationDAO dao = new ProgressionRegistrationDAO();
        int updated = dao.approvePending(registrationId, adminUserId);

        if (updated == 0) {
            throw new IllegalStateException("Registration not found or already decided.");
        }
    }

    public void rejectRegistration(long registrationId, long adminUserId, String reason) throws SQLException {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Reject reason is required.");
        }

        ProgressionRegistrationDAO dao = new ProgressionRegistrationDAO();
        int updated = dao.rejectPending(registrationId, adminUserId, reason.trim());

        if (updated == 0) {
            throw new IllegalStateException("Registration not found or already decided.");
        }
    }

    public List<ProgressionRegistration> listMyRegistrations(long createdByUserId) throws SQLException {
        ProgressionRegistrationDAO dao = new ProgressionRegistrationDAO();
        return dao.listByCreator(createdByUserId);
    }

    public List<ProgressionRegistration> listAllRegistrations() throws SQLException {
        ProgressionRegistrationDAO dao = new ProgressionRegistrationDAO();
        return dao.findAll();
    }
}