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

@WebServlet(urlPatterns = {"/academic/recovery_plan", "/admin/recovery_plan"})
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

        if (enrolmentIdStr == null || enrolmentIdStr.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing enrolment_id");
            return;
        }

        try {
            long enrolmentId = Long.parseLong(enrolmentIdStr);
            RecoveryPlan plan = recoveryPlanEJB.getPlanByEnrolmentId(enrolmentId);
            List<Milestone> milestones = milestoneEJB.list(enrolmentId);

            req.setAttribute("enrolmentId", enrolmentId);
            req.setAttribute("plan", plan);
            req.setAttribute("milestones", milestones);

            // Change 1C — pass dynamic action URL so JSP posts back to the correct path
            req.setAttribute("planActionUrl", req.getContextPath() + req.getServletPath());

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid enrolment_id");
            return;
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load plan: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/recovery_plan.jsp").forward(req, resp);
    }

    // ─────────────────────────────────────────────
    // doPost — enrolment-based actions only
    // ─────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String enrolmentIdStr = req.getParameter("enrolment_id");
        long userId = (long) req.getSession().getAttribute("userId");

        if (enrolmentIdStr == null || enrolmentIdStr.isBlank()) {
            // send them to a page where they can select an enrolment
            resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
            return;
        }

        long enrolmentId;
        try {
            enrolmentId = Long.parseLong(enrolmentIdStr);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid enrolment_id");
            return;
        }

        // Change 1B — dynamic base path so admin stays on /admin, academic stays on /academic
        String base = req.getContextPath() + req.getServletPath();

        try {
            if ("createPlan".equals(action)) {
                String recommendation = req.getParameter("recommendation");
                recoveryPlanEJB.createPlanForApprovedEnrolment(enrolmentId, recommendation, userId);
                resp.sendRedirect(base + "?enrolment_id=" + enrolmentId);
                return;

            } else if ("updatePlan".equals(action)) {
                String recommendation = req.getParameter("recommendation");
                recoveryPlanEJB.updateRecommendation(enrolmentId, recommendation);
                resp.sendRedirect(base + "?enrolment_id=" + enrolmentId);
                return;

            } else if ("addMilestone".equals(action)) {
                String title = req.getParameter("title");
                String due = req.getParameter("due_date"); // yyyy-MM-dd
                LocalDate dueDate = (due == null || due.isBlank()) ? null : LocalDate.parse(due);
                String remarks = req.getParameter("remarks");
                milestoneEJB.add(enrolmentId, title, dueDate, remarks);
                resp.sendRedirect(base + "?enrolment_id=" + enrolmentId);
                return;

            } else if ("updateMilestone".equals(action)) {
                long milestoneId = Long.parseLong(req.getParameter("milestone_id"));
                String status = req.getParameter("status");
                String remarks = req.getParameter("remarks");
                milestoneEJB.updateStatus(milestoneId, status, remarks);
                resp.sendRedirect(base + "?enrolment_id=" + enrolmentId);
                return;

            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown or unsupported action.");
                return;
            }

        } catch (SecurityException | IllegalStateException | IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            req.setAttribute("error", "Failed: " + e.getMessage());
        }

        // On error: reload plan page with error message
        doGet(req, resp);
    }
}