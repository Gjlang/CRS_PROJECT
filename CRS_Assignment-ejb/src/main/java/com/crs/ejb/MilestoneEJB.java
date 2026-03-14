package com.crs.ejb;

import com.crs.dao.MilestoneDAO;
import com.crs.dao.RecoveryPlanDAO;
import com.crs.entity.Milestone;
import com.crs.entity.RecoveryPlan;
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class MilestoneEJB {

    private final RecoveryPlanDAO planDAO = new RecoveryPlanDAO();
    private final MilestoneDAO milestoneDAO = new MilestoneDAO();

    public List<Milestone> list(String studentId, String courseCode, int attemptNo) throws SQLException {
        RecoveryPlan plan = planDAO.findByStudentCourseAttempt(studentId, courseCode, attemptNo);
        if (plan == null) return List.of();
        return milestoneDAO.listByPlanId(plan.getPlanId());
    }

    public void add(String studentId, String courseCode, int attemptNo,
                    String title, String task, LocalDate dueDate, String remarks) throws SQLException {

        RecoveryPlan plan = planDAO.findByStudentCourseAttempt(studentId, courseCode, attemptNo);
        if (plan == null) {
            throw new IllegalStateException("Create Recovery Plan first.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Milestone title is required.");
        }
        if (task == null || task.isBlank()) {
            throw new IllegalArgumentException("Milestone task is required.");
        }

        Milestone m = new Milestone();
        m.setPlanId(plan.getPlanId());
        m.setStudyWeek(0); // important fix so insert always has a value
        m.setTitle(title.trim());
        m.setTask(task.trim());
        m.setDueDate(dueDate);
        m.setStatus("PENDING");
        m.setRemarks(remarks == null ? "" : remarks.trim());

        milestoneDAO.insert(m);
    }

    public void updateStatus(long milestoneId, String status, String remarks) throws SQLException {
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
        milestoneDAO.updateStatus(milestoneId, status.trim(), remarks == null ? "" : remarks.trim());
    }
}