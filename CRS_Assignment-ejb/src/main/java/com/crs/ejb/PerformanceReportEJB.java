package com.crs.ejb;

import com.crs.dao.CourseDAO;
import com.crs.dao.StudentDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.ejb.dto.ReportCourseRow;
import com.crs.ejb.dto.ReportData;
import com.crs.entity.Course;
import com.crs.entity.Student;
import com.crs.entity.StudentResult;
import jakarta.ejb.Stateless;
import java.sql.SQLException;
import java.util.*;

@Stateless
public class PerformanceReportEJB {

    public ReportData generateReport(String studentId, int semester, int year, int yearOfStudy) throws SQLException {
        if (studentId == null || studentId.isBlank()) throw new IllegalArgumentException("studentId required");
        studentId = studentId.trim();

        StudentDAO studentDAO = new StudentDAO();
        Student s = studentDAO.findById(studentId);
        if (s == null) throw new IllegalArgumentException("Student not found: " + studentId);

        StudentResultDAO resultDAO = new StudentResultDAO();
        List<StudentResult> results = resultDAO.reportBySemesterYear(studentId, semester, year, yearOfStudy);

        // Aggregate per course (weighted grade points + display grade is simplified: show last component grade)
        Map<String, List<StudentResult>> byCourse = new LinkedHashMap<>();
        for (StudentResult r : results) {
            byCourse.computeIfAbsent(r.getCourseCode(), k -> new ArrayList<>()).add(r);
        }

        CourseDAO courseDAO = new CourseDAO();
        List<ReportCourseRow> rows = new ArrayList<>();
        for (String courseCode : byCourse.keySet()) {
            Course c = courseDAO.findByCode(courseCode);
            String title = (c != null) ? c.getCourseTitle() : courseCode;
            int credits = (c != null) ? c.getCreditHours() : 0;

            // For UI simplicity, show the grade/GP of the last row; CGPA is computed in StudentDAO.
            StudentResult last = byCourse.get(courseCode).get(byCourse.get(courseCode).size() - 1);
            rows.add(new ReportCourseRow(courseCode, title, credits, last.getGrade(), last.getGradePoint()));
        }

        double cgpa = studentDAO.calculateCgpaBySemesterYear(studentId, semester, year, yearOfStudy);

        ReportData rd = new ReportData();
        rd.setStudentId(studentId);
        rd.setStudentName(s.getStudentName());
        rd.setProgram(s.getProgram());
        rd.setSemester(semester);
        rd.setYear(year);
        rd.setYearOfStudy(yearOfStudy);
        rd.setRows(rows);
        rd.setCgpa(cgpa);
        return rd;
    }
}

