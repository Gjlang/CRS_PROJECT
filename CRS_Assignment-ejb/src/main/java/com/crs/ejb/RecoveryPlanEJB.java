package com.crs.ejb;

import com.crs.dao.MilestoneDAO;
import com.crs.dao.RecoveryPlanDAO;
import com.crs.dao.StudentDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.entity.Milestone;
import com.crs.entity.RecoveryPlan;
import com.crs.entity.Student;
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

        studentId = studentId.trim().toUpperCase();
        courseCode = courseCode.trim().toUpperCase();

        List<StudentResult> recoveryComponents = studentResultDAO.findComponentsForRecovery(studentId, courseCode, attemptNo);
        if (recoveryComponents == null || recoveryComponents.isEmpty()) {
            throw new IllegalStateException("Recovery plan can only be created for a valid recovery course attempt.");
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

        long planId = planDAO.insert(p);

        notifyStudent(studentId, courseCode, attemptNo, p.getRecommendation());

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

        String normalized = recommendation == null ? "" : recommendation.trim();
        planDAO.updateRecommendation(plan.getPlanId(), normalized);

        notifyStudent(studentId, courseCode, attemptNo, normalized);
    }

    public void removeRecommendation(String studentId, String courseCode, int attemptNo) throws SQLException {
        RecoveryPlan plan = planDAO.findByStudentCourseAttempt(studentId, courseCode, attemptNo);
        if (plan == null) {
            throw new IllegalStateException("No recovery plan exists for this student/course/attempt.");
        }

        planDAO.updateRecommendation(plan.getPlanId(), "");

        notifyStudent(studentId, courseCode, attemptNo, "");
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

        return new MilestoneDAO().create(m);
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

    private void notifyStudent(String studentId, String courseCode, int attemptNo, String recommendation) {
        try {
            Student student = new StudentDAO().findById(studentId);
            if (student != null && student.getEmail() != null && !student.getEmail().isBlank() && notificationEJB != null) {
                notificationEJB.sendRecoveryPlanEmail(student.getEmail(), studentId, courseCode, attemptNo, recommendation);
            }
        } catch (Exception ignored) {
        }
    }
}