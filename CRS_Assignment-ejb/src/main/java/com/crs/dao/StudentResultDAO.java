package com.crs.dao;

import com.crs.ejb.dto.AdminStudentResultRow;
import com.crs.entity.StudentResult;
import com.crs.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentResultDAO {

    public List<StudentResult> findResults(String studentId, String courseCode, int attemptNo) throws SQLException {
        String sql = "SELECT * FROM student_results WHERE student_id=? AND course_code=? AND attempt_no=? ORDER BY assessment_id";
        List<StudentResult> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            ps.setInt(3, attemptNo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<StudentResult> findFailedComponents(String studentId, String courseCode, int attemptNo) throws SQLException {
        String sql = "SELECT * FROM student_results WHERE student_id=? AND course_code=? AND attempt_no=? AND failed=1 ORDER BY assessment_id";
        List<StudentResult> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, courseCode);
            ps.setInt(3, attemptNo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<StudentResult> reportBySemesterYear(String studentId, int semester, int year, int yearOfStudy) throws SQLException {
        String sql = "SELECT * FROM student_results WHERE student_id=? AND semester=? AND year=? AND year_of_study=? ORDER BY course_code, assessment_id";
        List<StudentResult> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setInt(2, semester);
            ps.setInt(3, year);
            ps.setInt(4, yearOfStudy);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<AdminStudentResultRow> findDetailedResultsByStudent(String studentId) throws SQLException {
        String sql = """
            SELECT
                r.result_id,
                r.course_code,
                c.CourseName,
                a.component_name,
                r.grade,
                r.grade_point,
                r.failed,
                r.attempt_no,
                r.semester,
                r.year,
                r.year_of_study
            FROM student_results r
            LEFT JOIN assessments a
                   ON a.assessment_id = r.assessment_id
            LEFT JOIN courses c
                   ON c.CourseID = r.course_code
            WHERE r.student_id = ?
            ORDER BY r.year DESC, r.semester DESC, r.course_code, r.attempt_no, a.component_name
            """;

        List<AdminStudentResultRow> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdminStudentResultRow row = new AdminStudentResultRow();
                    row.setResultId(rs.getLong("result_id"));
                    row.setCourseCode(rs.getString("course_code"));
                    row.setCourseName(rs.getString("CourseName"));
                    row.setAssessmentComponent(rs.getString("component_name"));
                    row.setGrade(rs.getString("grade"));
                    row.setGradePoint(rs.getDouble("grade_point"));
                    row.setFailed(rs.getBoolean("failed"));
                    row.setAttemptNo(rs.getInt("attempt_no"));
                    row.setSemester(rs.getInt("semester"));
                    row.setYear(rs.getInt("year"));
                    row.setYearOfStudy(rs.getInt("year_of_study"));
                    list.add(row);
                }
            }
        }

        return list;
    }

    public StudentResult findLatestStudyContext(String studentId) throws SQLException {
        String sql = """
            SELECT *
            FROM student_results
            WHERE student_id = ?
            ORDER BY year DESC, semester DESC, year_of_study DESC, attempt_no DESC, result_id DESC
            LIMIT 1
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private StudentResult map(ResultSet rs) throws SQLException {
        return new StudentResult(
                rs.getLong("result_id"),
                rs.getString("student_id"),
                rs.getString("course_code"),
                rs.getLong("assessment_id"),
                rs.getInt("attempt_no"),
                rs.getString("grade"),
                rs.getDouble("grade_point"),
                rs.getBoolean("failed"),
                rs.getInt("semester"),
                rs.getInt("year"),
                rs.getInt("year_of_study")
        );
    }
}