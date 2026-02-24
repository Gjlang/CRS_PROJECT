package com.crs.ejb;

import com.crs.dao.AssessmentDAO;
import com.crs.dao.MilestoneDAO;
import com.crs.dao.RecoveryPlanDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.entity.Assessment;
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

    @EJB
    private NotificationEJB notificationEJB;

    public RecoveryPlan getOrCreatePlan(String studentId, String courseCode, int attemptNo, long createdByUserId) throws SQLException {
        if (attemptNo < 1 || attemptNo > 3) throw new IllegalArgumentException("attemptNo must be 1..3");
        if (studentId == null || studentId.isBlank()) throw new IllegalArgumentException("studentId required");
        if (courseCode == null || courseCode.isBlank()) throw new IllegalArgumentException("courseCode required");

        studentId = studentId.trim();
        courseCode = courseCode.trim().toUpperCase();

        RecoveryPlanDAO dao = new RecoveryPlanDAO();
        RecoveryPlan p = dao.findByStudentCourseAttempt(studentId, courseCode, attemptNo);
        if (p != null) return p;

        // Start with default recommendation string based on attempt policy
        String recommendation = defaultRecommendation(studentId, courseCode, attemptNo);

        RecoveryPlan np = new RecoveryPlan();
        np.setStudentId(studentId);
        np.setCourseCode(courseCode);
        np.setAttemptNo(attemptNo);
        np.setRecommendation(recommendation);
        np.setCreatedByUserId(createdByUserId);

        long id = dao.create(np);
        np.setPlanId(id);

        if (notificationEJB != null) {
            notificationEJB.sendMilestoneReminderEmail(
                "student@example.com",
                "Recovery plan created: " + courseCode + " (Attempt " + attemptNo + ")",
                recommendation);
        }
        return np;
    }

    public void updateRecommendation(long planId, String text) throws SQLException {
        if (planId <= 0) throw new IllegalArgumentException("planId invalid");
        if (text == null) text = "";
        new RecoveryPlanDAO().updateRecommendation(planId, text);
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
        m.setStatus("TODO");
        m.setGrade(null);

        long id = new MilestoneDAO().create(m);

        if (notificationEJB != null) {
            notificationEJB.sendMilestoneReminderEmail(
                "student@example.com",
                "New milestone added (Plan " + planId + ")",
                "Week " + studyWeek + ": " + task + (dueDate != null ? (" (Due: " + dueDate + ")") : ""));
        }
        return id;
    }

    public void updateMilestone(Milestone m) throws SQLException {
        if (m == null || m.getMilestoneId() <= 0) throw new IllegalArgumentException("milestone invalid");
        new MilestoneDAO().update(m);
    }

    public void deleteMilestone(long milestoneId) throws SQLException {
        if (milestoneId <= 0) throw new IllegalArgumentException("milestoneId invalid");
        new MilestoneDAO().delete(milestoneId);
    }

    public void markDone(long milestoneId, String grade) throws SQLException {
        if (milestoneId <= 0) throw new IllegalArgumentException("milestoneId invalid");
        new MilestoneDAO().markDone(milestoneId, grade);
    }

    // Attempt policy:
    // attempt 2: focus on failed components only
    // attempt 3: must include all components
    private String defaultRecommendation(String studentId, String courseCode, int attemptNo) throws SQLException {
        AssessmentDAO assessmentDAO = new AssessmentDAO();
        StudentResultDAO resultDAO = new StudentResultDAO();

        StringBuilder sb = new StringBuilder();
        sb.append("Policy for Attempt ").append(attemptNo).append(": ");
        if (attemptNo == 2) {
            sb.append("Resubmit/Resit FAILED components only.\n");
            List<StudentResult> failed = resultDAO.findFailedComponents(studentId, courseCode, 1);
            sb.append("Failed components from Attempt 1:\n");
            for (StudentResult r : failed) {
                sb.append("- assessment_id ").append(r.getAssessmentId()).append("\n");
            }
        } else if (attemptNo == 3) {
            sb.append("Must refer to ALL assessment components.\n");
            List<Assessment> all = assessmentDAO.findByCourse(courseCode);
            sb.append("All components:\n");
            for (Assessment a : all) {
                sb.append("- ").append(a.getComponentName()).append(" (").append(a.getWeightPercent()).append("%)\n");
            }
        } else {
            sb.append("Attempt 1: normal study and reassessment rules apply.\n");
        }
        return sb.toString();
    }
}

