package com.crs.dao;

import com.crs.entity.Student;
import com.crs.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public Student findById(String studentId) throws SQLException {
        String sql = "SELECT * FROM students WHERE StudentID = ?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Student> findAllActive() throws SQLException {
        String sql = "SELECT * FROM students WHERE active = 1 ORDER BY StudentID";
        List<Student> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public double calculateCgpaOverall(String studentId) throws SQLException {
        String sql = """
            SELECT
                COALESCE(SUM(sr.grade_point * c.Credits), 0) / NULLIF(SUM(c.Credits), 0) AS cgpa
            FROM student_results sr
            JOIN courses c ON c.CourseID = sr.course_code
            WHERE sr.student_id = ?
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("cgpa");
                }
            }
        }
        return 0.0;
    }

    public double calculateCgpaBySemesterYear(String studentId, int semester, int year, int yearOfStudy) throws SQLException {
        String sql = """
            SELECT
                COALESCE(SUM(sr.grade_point * c.Credits), 0) / NULLIF(SUM(c.Credits), 0) AS cgpa
            FROM student_results sr
            JOIN courses c ON c.CourseID = sr.course_code
            WHERE sr.student_id = ?
              AND sr.semester = ?
              AND sr.year = ?
              AND sr.year_of_study = ?
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setInt(2, semester);
            ps.setInt(3, year);
            ps.setInt(4, yearOfStudy);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("cgpa");
                }
            }
        }
        return 0.0;
    }
    
    

    public int countFailedCourses(String studentId) throws SQLException {
        String sql = """
            SELECT COUNT(DISTINCT course_code) AS failed_count
            FROM student_results
            WHERE student_id = ?
              AND (
                    failed = 1
                    OR UPPER(COALESCE(grade, '')) IN ('F', 'FAIL')
              )
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("failed_count");
                }
            }
        }
        return 0;
    }
    
    public List<Student> findAllActiveStudents() throws SQLException {
        String sql = """
            SELECT StudentID, FirstName, LastName, Major, Year, Email, active
            FROM students
            WHERE active = 1
            ORDER BY StudentID
            """;

        List<Student> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }


    private Student map(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getString("StudentID"));
        s.setFirstName(rs.getString("FirstName"));
        s.setLastName(rs.getString("LastName"));
        s.setMajor(rs.getString("Major"));
        s.setYear(rs.getInt("Year"));
        s.setEmail(rs.getString("Email"));
        s.setActive(rs.getBoolean("active"));
        return s;
    }
}