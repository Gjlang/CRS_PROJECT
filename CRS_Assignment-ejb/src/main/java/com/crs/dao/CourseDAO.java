package com.crs.dao;

import com.crs.entity.Course;
import com.crs.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public Course findByCode(String courseCode) throws SQLException {
        String sql = "SELECT course_code, course_title, credit_hours FROM courses WHERE course_code=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Course> findAll() throws SQLException {
        String sql = "SELECT course_code, course_title, credit_hours FROM courses ORDER BY course_code";
        List<Course> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void create(Course c) throws SQLException {
        String sql = "INSERT INTO courses(course_code, course_title, credit_hours) VALUES(?,?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseCode());
            ps.setString(2, c.getCourseTitle());
            ps.setInt(3, c.getCreditHours());
            ps.executeUpdate();
        }
    }

    public void update(Course c) throws SQLException {
        String sql = "UPDATE courses SET course_title=?, credit_hours=? WHERE course_code=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseTitle());
            ps.setInt(2, c.getCreditHours());
            ps.setString(3, c.getCourseCode());
            ps.executeUpdate();
        }
    }

    private Course map(ResultSet rs) throws SQLException {
        return new Course(
                rs.getString("course_code"),
                rs.getString("course_title"),
                rs.getInt("credit_hours")
        );
    }
}

