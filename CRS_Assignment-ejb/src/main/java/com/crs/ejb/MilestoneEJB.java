package com.crs.ejb;

import com.crs.dao.MilestoneDAO;
import com.crs.dao.RecoveryPlanDAO;
import com.crs.dao.StudentDAO;
import com.crs.entity.Milestone;
import com.crs.entity.RecoveryPlan;
import com.crs.entity.Student;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class MilestoneEJB {

    private final RecoveryPlanDAO planDAO = new RecoveryPlanDAO();
    private final MilestoneDAO milestoneDAO = new MilestoneDAO();

    @EJB
    private NotificationEJB notificationEJB;

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
        m.setStudyWeek(0);
        m.setTitle(title.trim());
        m.setTask(task.trim());
        m.setDueDate(dueDate);
        m.setStatus("PENDING");
        m.setGrade(null);
        m.setRemarks(remarks == null ? "" : remarks.trim());

        milestoneDAO.insert(m);

        notifyStudent(studentId, courseCode, attemptNo, title, "ADDED", null, m.getRemarks());
    }

    public void updateProgress(String studentId, String courseCode, int attemptNo,
                               long milestoneId, String status, String grade, String remarks) throws SQLException {

        if (status == null || status.isBlank()) {
            status = "PENDING";
        }

        String normalizedRemarks = remarks == null ? "" : remarks.trim();
        String normalizedGrade = (grade == null || grade.isBlank()) ? null : grade.trim();

        milestoneDAO.updateProgress(milestoneId, status.trim(), normalizedGrade, normalizedRemarks);

        notifyStudent(studentId, courseCode, attemptNo, "Milestone ID " + milestoneId, status.trim(), normalizedGrade, normalizedRemarks);
    }

    public void deleteMilestone(String studentId, String courseCode, int attemptNo, long milestoneId) throws SQLException {
        milestoneDAO.delete(milestoneId);
        notifyStudent(studentId, courseCode, attemptNo, "Milestone ID " + milestoneId, "DELETED", null, "");
    }

    public void updateStatus(long milestoneId, String status, String remarks) throws SQLException {
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
        milestoneDAO.updateStatus(milestoneId, status.trim(), remarks == null ? "" : remarks.trim());
    }

    private void notifyStudent(String studentId, String courseCode, int attemptNo,
                               String title, String status, String grade, String remarks) {
        try {
            Student student = new StudentDAO().findById(studentId);
            if (student != null && student.getEmail() != null && !student.getEmail().isBlank() && notificationEJB != null) {
                notificationEJB.sendMilestoneEmail(
                        student.getEmail(),
                        status,
                        studentId,
                        courseCode,
                        attemptNo,
                        title,
                        status,
                        grade,
                        remarks
                );
            }
        } catch (Exception ignored) {
        }
    }
}