package com.crs.dao;

import com.crs.entity.Student;
import com.crs.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public Student findById(String studentId) throws SQLException {
        String sql = "SELECT student_id, student_name, program, year_of_study, active FROM students WHERE student_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Student> findAllActive() throws SQLException {
        String sql = "SELECT student_id, student_name, program, year_of_study, active FROM students WHERE active=1 ORDER BY student_id";
        List<Student> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public int countFailedCourses(String studentId) throws SQLException {
        // Count distinct courses where the latest attempt has at least one failed component.
        String sql = """
            SELECT COUNT(*) AS total_failed FROM (
              SELECT latest.course_code AS course_code
              FROM (
                SELECT course_code, MAX(attempt_no) AS max_attempt
                FROM student_results
                WHERE student_id=?
                GROUP BY course_code
              ) latest
              JOIN student_results r
                ON r.student_id=?
               AND r.course_code=latest.course_code
               AND r.attempt_no=latest.max_attempt
              GROUP BY latest.course_code
              HAVING SUM(CASE WHEN r.failed=1 THEN 1 ELSE 0 END) > 0
            ) x
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("total_failed") : 0;
            }
        }
    }

    public double calculateCgpaOverall(String studentId) throws SQLException {
        String sql = """
            SELECT
              SUM(course_gp * credit_hours) / NULLIF(SUM(credit_hours),0) AS cgpa
            FROM (
              SELECT r.course_code,
                     c.credit_hours,
                     SUM(r.grade_point * (a.weight_percent/100.0)) AS course_gp
              FROM student_results r
              JOIN assessments a ON a.assessment_id = r.assessment_id
              JOIN courses c ON c.course_code = r.course_code
              WHERE r.student_id=?
              GROUP BY r.course_code, c.credit_hours, r.attempt_no, r.year, r.semester
            ) course_sem
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                double cgpa = rs.next() ? rs.getDouble("cgpa") : 0.0;
                return Double.isNaN(cgpa) ? 0.0 : cgpa;
            }
        }
    }

    public double calculateCgpaBySemesterYear(String studentId, int semester, int year, int yearOfStudy) throws SQLException {
        String sql = """
            SELECT
              SUM(course_gp * credit_hours) / NULLIF(SUM(credit_hours),0) AS cgpa
            FROM (
              SELECT r.course_code,
                     c.credit_hours,
                     SUM(r.grade_point * (a.weight_percent/100.0)) AS course_gp
              FROM student_results r
              JOIN assessments a ON a.assessment_id = r.assessment_id
              JOIN courses c ON c.course_code = r.course_code
              WHERE r.student_id=? AND r.semester=? AND r.year=? AND r.year_of_study=?
              GROUP BY r.course_code, c.credit_hours
            ) course_level
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setInt(2, semester);
            ps.setInt(3, year);
            ps.setInt(4, yearOfStudy);
            try (ResultSet rs = ps.executeQuery()) {
                double cgpa = rs.next() ? rs.getDouble("cgpa") : 0.0;
                return Double.isNaN(cgpa) ? 0.0 : cgpa;
            }
        }
    }

    public void create(Student s) throws SQLException {
        String sql = "INSERT INTO students(student_id, student_name, program, year_of_study, active) VALUES(?,?,?,?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.setString(2, s.getStudentName());
            ps.setString(3, s.getProgram());
            ps.setInt(4, s.getYearOfStudy());
            ps.setBoolean(5, s.isActive());
            ps.executeUpdate();
        }
    }

    public void update(Student s) throws SQLException {
        String sql = "UPDATE students SET student_name=?, program=?, year_of_study=?, active=? WHERE student_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getStudentName());
            ps.setString(2, s.getProgram());
            ps.setInt(3, s.getYearOfStudy());
            ps.setBoolean(4, s.isActive());
            ps.setString(5, s.getStudentId());
            ps.executeUpdate();
        }
    }

    public void deactivate(String studentId) throws SQLException {
        String sql = "UPDATE students SET active=0 WHERE student_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.executeUpdate();
        }
    }

    private Student map(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("student_id"),
                rs.getString("student_name"),
                rs.getString("program"),
                rs.getInt("year_of_study"),
                rs.getBoolean("active")
        );
    }
}

