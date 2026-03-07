package com.crs.dao;

import com.crs.entity.Course;
import com.crs.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public Course findByCode(String courseId) throws SQLException {
        String sql = """
            SELECT CourseID, CourseName, Credits, Semester, Instructor, Capacity
            FROM courses
            WHERE CourseID=?
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Course> findAll() throws SQLException {
        String sql = """
            SELECT CourseID, CourseName, Credits, Semester, Instructor, Capacity
            FROM courses
            ORDER BY CourseID
            """;
        List<Course> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void create(Course c) throws SQLException {
        String sql = """
            INSERT INTO courses(CourseID, CourseName, Credits, Semester, Instructor, Capacity)
            VALUES(?,?,?,?,?,?)
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseId());
            ps.setString(2, c.getCourseName());
            ps.setInt(3, c.getCredits());
            ps.setString(4, c.getSemester());
            ps.setString(5, c.getInstructor());
            ps.setObject(6, c.getCapacity());
            ps.executeUpdate();
        }
    }

    public void update(Course c) throws SQLException {
        String sql = """
            UPDATE courses
            SET CourseName=?, Credits=?, Semester=?, Instructor=?, Capacity=?
            WHERE CourseID=?
            """;
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setInt(2, c.getCredits());
            ps.setString(3, c.getSemester());
            ps.setString(4, c.getInstructor());
            ps.setObject(5, c.getCapacity());
            ps.setString(6, c.getCourseId());
            ps.executeUpdate();
        }
    }

    private Course map(ResultSet rs) throws SQLException {
        return new Course(
                rs.getString("CourseID"),
                rs.getString("CourseName"),
                rs.getInt("Credits"),
                rs.getString("Semester"),
                rs.getString("Instructor"),
                (Integer) rs.getObject("Capacity")
        );
    }
}