package com.crs.ejb;

import com.crs.dao.EnrolmentDAO;
import com.crs.dao.MilestoneDAO;
import com.crs.dao.RecoveryPlanDAO;
import com.crs.entity.Enrolment;
import com.crs.entity.Milestone;
import com.crs.entity.RecoveryPlan;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class RecoveryPlanEJB {

    @EJB
    private NotificationEJB notificationEJB;

    private final EnrolmentDAO enrolmentDAO = new EnrolmentDAO();
    private final RecoveryPlanDAO planDAO = new RecoveryPlanDAO();

    // ─────────────────────────────────────────────
    // CORE — Enrolment-based plan management
    // ─────────────────────────────────────────────

    /**
     * STRICT: Only create a plan if the enrolment exists AND status is APPROVED.
     * One plan per enrolment enforced (DB unique + DAO check).
     */
    public long createPlanForApprovedEnrolment(long enrolmentId, String recommendation, long academicUserId)
            throws SQLException {
        // 1) Must be an APPROVED enrolment
        Enrolment e = enrolmentDAO.findApprovedById(enrolmentId);
        if (e == null) {
            throw new IllegalArgumentException("Invalid enrolment ID or enrolment is not APPROVED.");
        }
        if (!"APPROVED".equalsIgnoreCase(e.getEnrolmentStatus())) {
            throw new SecurityException("Recovery plan allowed only for APPROVED enrolment.");
        }

        // 2) Prevent duplicates
        RecoveryPlan existing = planDAO.findByEnrolmentId(enrolmentId);
        if (existing != null) {
            throw new IllegalStateException("Recovery plan already exists for this enrolment.");
        }

        RecoveryPlan p = new RecoveryPlan();
        p.setEnrolmentId(enrolmentId);
        p.setStudentId(e.getStudentId());
        p.setCourseCode(e.getCourseCode());
        p.setAttemptNo(e.getAttemptNo());
        p.setRecommendation(recommendation == null ? "" : recommendation.trim());
        p.setCreatedByUserId(academicUserId);

        long planId = planDAO.insert(p);

        // 3) Optional notification — non-crashing
        try {
            if (notificationEJB != null) {
                notificationEJB.sendMilestoneReminderEmail(
                    "student@example.com",
                    "Recovery plan created for enrolment #" + enrolmentId,
                    "Plan ID: " + planId);
            }
        } catch (Exception ignored) {}

        return planId;
    }

    /**
     * STRICT: Get plan only by enrolmentId.
     */
    public RecoveryPlan getPlanByEnrolmentId(long enrolmentId) throws SQLException {
        return planDAO.findByEnrolmentId(enrolmentId);
    }

    /**
     * STRICT: Update recommendation only if enrolment is APPROVED and plan exists.
     */
    public void updateRecommendation(long enrolmentId, String recommendation) throws SQLException {
        Enrolment e = enrolmentDAO.findApprovedById(enrolmentId);
        if (e == null) {
            throw new IllegalArgumentException("Invalid enrolment ID.");
        }
        if (!"APPROVED".equalsIgnoreCase(e.getEnrolmentStatus())) {
            throw new SecurityException("Cannot update plan unless enrolment is APPROVED.");
        }

        RecoveryPlan p = planDAO.findByEnrolmentId(enrolmentId);
        if (p == null) throw new IllegalStateException("No plan exists for this enrolment.");

        planDAO.updateRecommendation(p.getPlanId(), recommendation == null ? "" : recommendation.trim());
    }

    // ─────────────────────────────────────────────
    // MILESTONES
    // ─────────────────────────────────────────────

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

        try {
            if (notificationEJB != null) {
                notificationEJB.sendMilestoneReminderEmail(
                    "student@example.com",
                    "New milestone added (Plan " + planId + ")",
                    "Week " + studyWeek + ": " + task + (dueDate != null ? (" (Due: " + dueDate + ")") : ""));
            }
        } catch (Exception ignored) {}

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
}