package com.crs.dao;
import com.crs.entity.Milestone;
import com.crs.util.DbUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class MilestoneDAO {

    // 3.3 — new: list by planId (uses title, due_date, status, remarks columns)
    public List<Milestone> listByPlanId(long planId) throws SQLException {
        String sql = "SELECT milestone_id, plan_id, study_week, task, title, due_date, status, grade, remarks " +
                     "FROM milestones WHERE plan_id=? ORDER BY due_date ASC";
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

    // Original — find by plan ordered by study_week
    public List<Milestone> findByPlan(long planId) throws SQLException {
        String sql = "SELECT milestone_id, plan_id, study_week, task, title, due_date, status, grade, remarks " +
                     "FROM milestones WHERE plan_id=? ORDER BY study_week";
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

    // 3.3 — new insert() (uses title, remarks)
    public long insert(Milestone m) throws SQLException {
        String sql = "INSERT INTO milestones(plan_id, study_week, task, title, due_date, status, grade, remarks) " +
                     "VALUES(?,?,?,?,?,?,?,?)";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, m.getPlanId());
            ps.setInt(2, m.getStudyWeek());
            ps.setString(3, m.getTask());
            ps.setString(4, m.getTitle());
            if (m.getDueDate() == null) ps.setNull(5, Types.DATE);
            else ps.setDate(5, Date.valueOf(m.getDueDate()));
            ps.setString(6, m.getStatus() == null ? "PENDING" : m.getStatus());
            ps.setString(7, m.getGrade());
            ps.setString(8, m.getRemarks());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                throw new SQLException("Failed to get generated milestone_id");
            }
        }
    }

    // Original create() — kept for backward compatibility, delegates to insert()
    /** @deprecated Use insert(Milestone) instead */
    @Deprecated
    public long create(Milestone m) throws SQLException {
        return insert(m);
    }

    // Original update() — full update by milestone_id (unchanged)
    public void update(Milestone m) throws SQLException {
        String sql = "UPDATE milestones SET study_week=?, task=?, title=?, due_date=?, status=?, grade=?, remarks=? " +
                     "WHERE milestone_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, m.getStudyWeek());
            ps.setString(2, m.getTask());
            ps.setString(3, m.getTitle());
            ps.setDate(4, (m.getDueDate() == null) ? null : Date.valueOf(m.getDueDate()));
            ps.setString(5, m.getStatus());
            ps.setString(6, m.getGrade());
            ps.setString(7, m.getRemarks());
            ps.setLong(8, m.getMilestoneId());
            ps.executeUpdate();
        }
    }

    // 3.3 — new: update status + remarks only (lightweight update)
    public void updateStatus(long milestoneId, String status, String remarks) throws SQLException {
        String sql = "UPDATE milestones SET status=?, remarks=? WHERE milestone_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setLong(3, milestoneId);
            ps.executeUpdate();
        }
    }

    // Original markDone() — kept, sets status=DONE + grade
    public void markDone(long milestoneId, String grade) throws SQLException {
        String sql = "UPDATE milestones SET status='DONE', grade=? WHERE milestone_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, grade);
            ps.setLong(2, milestoneId);
            ps.executeUpdate();
        }
    }

    // Original delete() — unchanged
    public void delete(long milestoneId) throws SQLException {
        String sql = "DELETE FROM milestones WHERE milestone_id=?";
        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, milestoneId);
            ps.executeUpdate();
        }
    }

    // Shared map() — handles both old and new columns safely
    private Milestone map(ResultSet rs) throws SQLException {
        Date d = rs.getDate("due_date");
        LocalDate due = (d == null) ? null : d.toLocalDate();
        Milestone m = new Milestone();
        m.setMilestoneId(rs.getLong("milestone_id"));
        m.setPlanId((Long) rs.getObject("plan_id"));
        m.setStudyWeek(rs.getInt("study_week"));
        m.setTask(rs.getString("task"));
        m.setTitle(rs.getString("title"));
        m.setDueDate(due);
        m.setStatus(rs.getString("status"));
        m.setGrade(rs.getString("grade"));
        m.setRemarks(rs.getString("remarks"));
        return m;
    }
}