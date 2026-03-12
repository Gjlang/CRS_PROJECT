package com.crs.ejb;

import com.crs.dao.MilestoneDAO;
import com.crs.dao.RecoveryPlanDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.entity.Milestone;
import com.crs.entity.RecoveryPlan;
import com.crs.entity.StudentResult;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class RecoveryPlanEJB {

    private final RecoveryPlanDAO planDAO = new RecoveryPlanDAO();
    private final StudentResultDAO studentResultDAO = new StudentResultDAO();

    @EJB
    private NotificationEJB notificationEJB;

    public long createPlan(String studentId, String courseCode, int attemptNo,
                           String recommendation, long academicUserId) throws SQLException {

        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("studentId is required.");
        }
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("courseCode is required.");
        }
        if (attemptNo <= 0) {
            throw new IllegalArgumentException("attemptNo must be > 0.");
        }

        studentId = studentId.trim();
        courseCode = courseCode.trim().toUpperCase();

        List<StudentResult> failedComponents = studentResultDAO.findFailedComponents(studentId, courseCode, attemptNo);
        if (failedComponents == null || failedComponents.isEmpty()) {
            throw new IllegalStateException("Recovery plan can only be created for a failed course attempt.");
        }

        RecoveryPlan existing = planDAO.findByStudentCourseAttempt(studentId, courseCode, attemptNo);
        if (existing != null) {
            throw new IllegalStateException("Recovery plan already exists for this student/course/attempt.");
        }

        RecoveryPlan p = new RecoveryPlan();
        p.setStudentId(studentId);
        p.setCourseCode(courseCode);
        p.setAttemptNo(attemptNo);
        p.setRecommendation(recommendation == null ? "" : recommendation.trim());
        p.setCreatedByUserId(academicUserId);
        p.setEnrolmentId(null);

        long planId = planDAO.insert(p);

        try {
            if (notificationEJB != null) {
                notificationEJB.sendMilestoneReminderEmail(
                        "student@example.com",
                        "Recovery plan created for " + studentId + " / " + courseCode,
                        "Plan ID: " + planId
                );
            }
        } catch (Exception ignored) {
        }

        return planId;
    }

    public RecoveryPlan getPlan(String studentId, String courseCode, int attemptNo) throws SQLException {
        return planDAO.findByStudentCourseAttempt(studentId, courseCode, attemptNo);
    }

    public void updateRecommendation(String studentId, String courseCode, int attemptNo, String recommendation) throws SQLException {
        RecoveryPlan plan = planDAO.findByStudentCourseAttempt(studentId, courseCode, attemptNo);
        if (plan == null) {
            throw new IllegalStateException("No recovery plan exists for this student/course/attempt.");
        }

        planDAO.updateRecommendation(plan.getPlanId(), recommendation == null ? "" : recommendation.trim());
    }

    public List<Milestone> listMilestones(long planId) throws SQLException {
        return new MilestoneDAO().findByPlan(planId);
    }

    public long addMilestone(long planId, int studyWeek, String task, LocalDate dueDate) throws SQLException {
        if (planId <= 0) throw new IllegalArgumentException("planId invalid");
        if (studyWeek <= 0) throw new IllegalArgumentException("studyWeek must be > 0");
        if (task == null || task.isBlank()) throw new IllegalArgumentException("task required");

        Milestone m = new Milestone();
        m.setPlanId(planId);
        m.setStudyWeek(studyWeek);
        m.setTask(task);
        m.setDueDate(dueDate);
        m.setStatus("PENDING");
        m.setGrade(null);

        long id = new MilestoneDAO().create(m);

        try {
            if (notificationEJB != null) {
                notificationEJB.sendMilestoneReminderEmail(
                        "student@example.com",
                        "New milestone added (Plan " + planId + ")",
                        "Week " + studyWeek + ": " + task + (dueDate != null ? (" (Due: " + dueDate + ")") : "")
                );
            }
        } catch (Exception ignored) {
        }

        return id;
    }

    public void updateMilestone(Milestone m) throws SQLException {
        if (m == null || m.getMilestoneId() <= 0) {
            throw new IllegalArgumentException("milestone invalid");
        }
        new MilestoneDAO().update(m);
    }

    public void deleteMilestone(long milestoneId) throws SQLException {
        if (milestoneId <= 0) {
            throw new IllegalArgumentException("milestoneId invalid");
        }
        new MilestoneDAO().delete(milestoneId);
    }

    public void markDone(long milestoneId, String grade) throws SQLException {
        if (milestoneId <= 0) {
            throw new IllegalArgumentException("milestoneId invalid");
        }
        new MilestoneDAO().markDone(milestoneId, grade);
    }
}