package com.crs.ejb;

import com.crs.dao.StudentDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.ejb.dto.EligibilityResult;
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class EligibilityEJB {

    public EligibilityResult checkStudent(String studentId) throws SQLException {
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("studentId is required.");
        }
        studentId = studentId.trim();

        StudentDAO dao = new StudentDAO();
        StudentResultDAO resultDAO = new StudentResultDAO();

        double cgpa = dao.calculateCgpaOverall(studentId);
        int failedCount = dao.countFailedCourses(studentId);

        List<String> reasons = new ArrayList<>();
        boolean eligible = true;

        if (cgpa < 2.0) {
            eligible = false;
            reasons.add("CGPA is below 2.0 (current: " + String.format("%.2f", cgpa) + ")");
        }
        if (failedCount > 3) {
            eligible = false;
            reasons.add("Failed courses exceed 3 (current: " + failedCount + ")");
        }
        if (eligible) {
            reasons.add("Eligible: CGPA >= 2.0 and failed courses <= 3.");
        }

        EligibilityResult r = new EligibilityResult();
        r.setStudentId(studentId);
        r.setCgpa(cgpa);
        r.setFailedCourseCount(failedCount);
        r.setEligible(eligible);
        r.setReasons(reasons);
        r.setGrades(resultDAO.findDetailedResultsByStudent(studentId));

        return r;
    }
}