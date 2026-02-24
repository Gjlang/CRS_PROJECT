package com.crs.dao;

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

