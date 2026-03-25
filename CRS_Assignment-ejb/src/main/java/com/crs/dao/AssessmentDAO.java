package com.crs.dao;

import com.crs.entity.Assessment;
import com.crs.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssessmentDAO {

    public List<Assessment> findByCourse(String courseCode) throws SQLException {
        String sql = "SELECT assessment_id, course_code, component_name, weight_percent FROM assessments WHERE course_code=? ORDER BY assessment_id";
        List<Assessment> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, courseCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public long create(Assessment a) throws SQLException {
        String sql = "INSERT INTO assessments(course_code, component_name, weight_percent) VALUES(?,?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getCourseCode());
            ps.setString(2, a.getComponentName());
            ps.setDouble(3, a.getWeightPercent());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    public void update(Assessment a) throws SQLException {
        String sql = "UPDATE assessments SET component_name=?, weight_percent=? WHERE assessment_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getComponentName());
            ps.setDouble(2, a.getWeightPercent());
            ps.setLong(3, a.getAssessmentId());
            ps.executeUpdate();
        }
    }

    public void delete(long assessmentId) throws SQLException {
        String sql = "DELETE FROM assessments WHERE assessment_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, assessmentId);
            ps.executeUpdate();
        }
    }
    
    public List<Assessment> findAll() throws SQLException {
        String sql = """
            SELECT assessment_id, course_code, component_name, weight_percent
            FROM assessments
            ORDER BY assessment_id DESC
            """;

        List<Assessment> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }

    private Assessment map(ResultSet rs) throws SQLException {
        return new Assessment(
                rs.getLong("assessment_id"),
                rs.getString("course_code"),
                rs.getString("component_name"),
                rs.getDouble("weight_percent")
        );
    }
}

