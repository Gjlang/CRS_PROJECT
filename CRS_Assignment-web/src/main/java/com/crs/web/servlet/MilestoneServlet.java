package com.crs.web.servlet;

import com.crs.ejb.RecoveryPlanEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/academic/milestones")
public class MilestoneServlet extends HttpServlet {

    @EJB
    private RecoveryPlanEJB recoveryPlanEJB;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        long planId = Long.parseLong(req.getParameter("plan_id"));

        try {
            if ("add".equals(action)) {
                int week = Integer.parseInt(req.getParameter("study_week"));
                String task = req.getParameter("task");
                String due = req.getParameter("due_date");
                LocalDate dueDate = (due == null || due.isBlank()) ? null : LocalDate.parse(due);
                recoveryPlanEJB.addMilestone(planId, week, task, dueDate);
                req.setAttribute("message", "Milestone added.");
            } else if ("delete".equals(action)) {
                long milestoneId = Long.parseLong(req.getParameter("milestone_id"));
                recoveryPlanEJB.deleteMilestone(milestoneId);
                req.setAttribute("message", "Milestone deleted.");
            } else if ("done".equals(action)) {
                long milestoneId = Long.parseLong(req.getParameter("milestone_id"));
                String grade = req.getParameter("grade");
                recoveryPlanEJB.markDone(milestoneId, grade);
                req.setAttribute("message", "Milestone marked DONE.");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Action failed: " + e.getMessage());
        }

        // Redirect back to recovery_plan page keeping plan loaded parameters
        resp.sendRedirect(req.getContextPath() + "/academic/recovery_plan.jsp?plan_id=" + planId);
    }
}

