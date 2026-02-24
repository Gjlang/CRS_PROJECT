package com.crs.dao;

import com.crs.entity.Milestone;
import com.crs.util.DbUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MilestoneDAO {

    public List<Milestone> findByPlan(long planId) throws SQLException {
        String sql = "SELECT milestone_id, plan_id, study_week, task, due_date, status, grade FROM milestones WHERE plan_id=? ORDER BY study_week";
        List<Milestone> list = new ArrayList<>();
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, planId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public long create(Milestone m) throws SQLException {
        String sql = "INSERT INTO milestones(plan_id, study_week, task, due_date, status, grade) VALUES(?,?,?,?,?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, m.getPlanId());
            ps.setInt(2, m.getStudyWeek());
            ps.setString(3, m.getTask());
            ps.setDate(4, (m.getDueDate() == null) ? null : Date.valueOf(m.getDueDate()));
            ps.setString(5, m.getStatus());
            ps.setString(6, m.getGrade());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    public void update(Milestone m) throws SQLException {
        String sql = "UPDATE milestones SET study_week=?, task=?, due_date=?, status=?, grade=? WHERE milestone_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getStudyWeek());
            ps.setString(2, m.getTask());
            ps.setDate(3, (m.getDueDate() == null) ? null : Date.valueOf(m.getDueDate()));
            ps.setString(4, m.getStatus());
            ps.setString(5, m.getGrade());
            ps.setLong(6, m.getMilestoneId());
            ps.executeUpdate();
        }
    }

    public void delete(long milestoneId) throws SQLException {
        String sql = "DELETE FROM milestones WHERE milestone_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, milestoneId);
            ps.executeUpdate();
        }
    }

    public void markDone(long milestoneId, String grade) throws SQLException {
        String sql = "UPDATE milestones SET status='DONE', grade=? WHERE milestone_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, grade);
            ps.setLong(2, milestoneId);
            ps.executeUpdate();
        }
    }

    private Milestone map(ResultSet rs) throws SQLException {
        Date d = rs.getDate("due_date");
        LocalDate due = (d == null) ? null : d.toLocalDate();
        return new Milestone(
                rs.getLong("milestone_id"),
                rs.getLong("plan_id"),
                rs.getInt("study_week"),
                rs.getString("task"),
                due,
                rs.getString("status"),
                rs.getString("grade")
        );
    }
}

