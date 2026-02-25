package com.crs.web.servlet;

import com.crs.ejb.MilestoneEJB;
import com.crs.ejb.RecoveryPlanEJB;
import com.crs.entity.Milestone;
import com.crs.entity.RecoveryPlan;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/academic/recovery_plan")
public class RecoveryPlanServlet extends HttpServlet {

    @EJB
    private RecoveryPlanEJB recoveryPlanEJB;

    @EJB
    private MilestoneEJB milestoneEJB;

    // ─────────────────────────────────────────────
    // doGet — load plan + milestones by enrolment_id
    // ─────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String enrolmentIdStr = req.getParameter("enrolment_id");

        if (enrolmentIdStr != null && !enrolmentIdStr.isBlank()) {
            try {
                long enrolmentId = Long.parseLong(enrolmentIdStr);
                RecoveryPlan plan = recoveryPlanEJB.getPlanByEnrolment(enrolmentId);
                List<Milestone> milestones = milestoneEJB.list(enrolmentId);

                req.setAttribute("enrolmentId", enrolmentId);
                req.setAttribute("plan", plan);
                req.setAttribute("milestones", milestones);
            } catch (Exception e) {
                req.setAttribute("error", "Failed to load plan: " + e.getMessage());
            }
        }

        req.getRequestDispatcher("/academic/recovery_plan.jsp").forward(req, resp);
    }

    // ─────────────────────────────────────────────
    // doPost — handle all actions
    // ─────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        long userId = (long) req.getSession().getAttribute("userId");

        try {
            // ── NEW: enrolment-based actions ──────────────────────────────

            if ("createPlan".equals(action)) {
                long enrolmentId = Long.parseLong(req.getParameter("enrolment_id"));
                String recommendation = req.getParameter("recommendation");
                recoveryPlanEJB.createPlan(enrolmentId, recommendation, userId);
                req.setAttribute("message", "Recovery plan created.");
                resp.sendRedirect(req.getContextPath() + "/academic/recovery_plan?enrolment_id=" + enrolmentId);
                return;

            } else if ("updatePlan".equals(action)) {
                long enrolmentId = Long.parseLong(req.getParameter("enrolment_id"));
                String recommendation = req.getParameter("recommendation");
                recoveryPlanEJB.updateRecommendation(enrolmentId, recommendation);
                req.setAttribute("message", "Recommendation updated.");
                resp.sendRedirect(req.getContextPath() + "/academic/recovery_plan?enrolment_id=" + enrolmentId);
                return;

            } else if ("addMilestone".equals(action)) {
                long enrolmentId = Long.parseLong(req.getParameter("enrolment_id"));
                String title = req.getParameter("title");
                String due = req.getParameter("due_date"); // yyyy-MM-dd
                LocalDate dueDate = (due == null || due.isBlank()) ? null : LocalDate.parse(due);
                String remarks = req.getParameter("remarks");
                milestoneEJB.add(enrolmentId, title, dueDate, remarks);
                resp.sendRedirect(req.getContextPath() + "/academic/recovery_plan?enrolment_id=" + enrolmentId);
                return;

            } else if ("updateMilestone".equals(action)) {
                long enrolmentId = Long.parseLong(req.getParameter("enrolment_id"));
                long milestoneId = Long.parseLong(req.getParameter("milestone_id"));
                String status = req.getParameter("status");
                String remarks = req.getParameter("remarks");
                milestoneEJB.updateStatus(milestoneId, status, remarks);
                resp.sendRedirect(req.getContextPath() + "/academic/recovery_plan?enrolment_id=" + enrolmentId);
                return;

            // ── LEGACY: student+course+attempt based actions ──────────────

            } else if ("load".equals(action)) {
                String studentId = req.getParameter("student_id");
                String courseCode = req.getParameter("course_code");
                int attemptNo = Integer.parseInt(req.getParameter("attempt_no"));
                RecoveryPlan plan = recoveryPlanEJB.getOrCreatePlan(studentId, courseCode, attemptNo, userId);
                req.setAttribute("plan", plan);
                req.setAttribute("milestones", recoveryPlanEJB.listMilestones(plan.getPlanId()));

            } else if ("update_recommendation".equals(action)) {
                long planId = Long.parseLong(req.getParameter("plan_id"));
                String rec = req.getParameter("recommendation");
                recoveryPlanEJB.updateRecommendation(planId, rec, true); // legacy overload
                req.setAttribute("message", "Recommendation updated.");
            }

        } catch (IllegalStateException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            req.setAttribute("error", "Failed: " + e.getMessage());
        }

        // Fall through to JSP for legacy actions or on error
        doGet(req, resp);
    }
}
