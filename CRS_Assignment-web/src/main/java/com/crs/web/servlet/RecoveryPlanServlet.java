package com.crs.web.servlet;

import com.crs.ejb.RecoveryPlanEJB;
import com.crs.entity.RecoveryPlan;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/academic/recovery_plan")
public class RecoveryPlanServlet extends HttpServlet {

    @EJB
    private RecoveryPlanEJB recoveryPlanEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/academic/recovery_plan.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("load".equals(action)) {
                String studentId = req.getParameter("student_id");
                String courseCode = req.getParameter("course_code");
                int attemptNo = Integer.parseInt(req.getParameter("attempt_no"));
                long createdBy = (long) req.getSession().getAttribute("userId");

                RecoveryPlan plan = recoveryPlanEJB.getOrCreatePlan(studentId, courseCode, attemptNo, createdBy);
                req.setAttribute("plan", plan);
                req.setAttribute("milestones", recoveryPlanEJB.listMilestones(plan.getPlanId()));
            } else if ("update_recommendation".equals(action)) {
                long planId = Long.parseLong(req.getParameter("plan_id"));
                String rec = req.getParameter("recommendation");
                recoveryPlanEJB.updateRecommendation(planId, rec);
                req.setAttribute("message", "Recommendation updated.");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Failed: " + e.getMessage());
        }
        req.getRequestDispatcher("/academic/recovery_plan.jsp").forward(req, resp);
    }
}

