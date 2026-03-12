package com.crs.web.servlet;

import com.crs.dao.StudentResultDAO;
import com.crs.ejb.MilestoneEJB;
import com.crs.ejb.RecoveryPlanEJB;
import com.crs.ejb.dto.RecoveryCandidateRow;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");
        String courseCode = req.getParameter("course_code");
        String attemptNoStr = req.getParameter("attempt_no");

        req.setAttribute("planActionUrl", req.getContextPath() + req.getServletPath());
        req.setAttribute("activePage", "academic_recovery_plan");

        try {
            // State A: first open page, no student selected yet
            if (studentId == null || studentId.isBlank()) {
                req.getRequestDispatcher("/academic/recovery_plan.jsp").forward(req, resp);
                return;
            }

            studentId = studentId.trim();
            req.setAttribute("studentId", studentId);

            // State B: show failed course attempts for selected student
            StudentResultDAO resultDAO = new StudentResultDAO();
            List<RecoveryCandidateRow> candidates = resultDAO.findRecoveryCandidatesByStudent(studentId);
            req.setAttribute("candidates", candidates);

            // If course/attempt not selected yet, just show candidate list
            if (courseCode == null || courseCode.isBlank() || attemptNoStr == null || attemptNoStr.isBlank()) {
                req.getRequestDispatcher("/academic/recovery_plan.jsp").forward(req, resp);
                return;
            }

            // State C: show selected plan
            int attemptNo = Integer.parseInt(attemptNoStr);

            RecoveryPlan plan = recoveryPlanEJB.getPlan(studentId, courseCode, attemptNo);
            List<Milestone> milestones = milestoneEJB.list(studentId, courseCode, attemptNo);

            req.setAttribute("courseCode", courseCode);
            req.setAttribute("attemptNo", attemptNo);
            req.setAttribute("plan", plan);
            req.setAttribute("milestones", milestones);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid attempt number.");
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load recovery plan: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/recovery_plan.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String studentId = req.getParameter("student_id");
        String courseCode = req.getParameter("course_code");
        String attemptNoStr = req.getParameter("attempt_no");

        Object userIdObj = req.getSession().getAttribute("userId");
        long userId = (userIdObj instanceof Long)
                ? (Long) userIdObj
                : Long.parseLong(String.valueOf(userIdObj));

        if (studentId == null || studentId.isBlank()
                || courseCode == null || courseCode.isBlank()
                || attemptNoStr == null || attemptNoStr.isBlank()) {
            req.setAttribute("error", "student_id, course_code, and attempt_no are required.");
            doGet(req, resp);
            return;
        }

        int attemptNo;
        try {
            attemptNo = Integer.parseInt(attemptNoStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid attempt_no.");
            doGet(req, resp);
            return;
        }

        String base = req.getContextPath() + req.getServletPath();

        try {
            if ("createPlan".equals(action)) {
                String recommendation = req.getParameter("recommendation");
                recoveryPlanEJB.createPlan(studentId, courseCode, attemptNo, recommendation, userId);

            } else if ("updatePlan".equals(action)) {
                String recommendation = req.getParameter("recommendation");
                recoveryPlanEJB.updateRecommendation(studentId, courseCode, attemptNo, recommendation);

            } else if ("addMilestone".equals(action)) {
                String title = req.getParameter("title");
                String task = req.getParameter("task");
                String due = req.getParameter("due_date");
                LocalDate dueDate = (due == null || due.isBlank()) ? null : LocalDate.parse(due);
                String remarks = req.getParameter("remarks");

                milestoneEJB.add(studentId, courseCode, attemptNo, title, task, dueDate, remarks);

            } else if ("updateMilestone".equals(action)) {
                long milestoneId = Long.parseLong(req.getParameter("milestone_id"));
                String status = req.getParameter("status");
                String remarks = req.getParameter("remarks");

                milestoneEJB.updateStatus(milestoneId, status, remarks);
            }

            resp.sendRedirect(base + "?student_id=" + studentId + "&course_code=" + courseCode + "&attempt_no=" + attemptNo);
            return;

        } catch (Exception e) {
            req.setAttribute("error", "Failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}