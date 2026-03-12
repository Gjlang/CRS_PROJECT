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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/academic/recovery_plan", "/admin/recovery_plan"})
public class RecoveryPlanServlet extends HttpServlet {

    @EJB
    private RecoveryPlanEJB recoveryPlanEJB;

    @EJB
    private MilestoneEJB milestoneEJB;

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    String keyword = req.getParameter("q");
	    String studentId = req.getParameter("student_id");
	    String courseCode = req.getParameter("course_code");
	    String attemptNoStr = req.getParameter("attempt_no");
	
	    if (keyword != null) keyword = keyword.trim();
	    if (studentId != null) studentId = studentId.trim().toUpperCase();
	    if (courseCode != null) courseCode = courseCode.trim().toUpperCase();
	    if (attemptNoStr != null) attemptNoStr = attemptNoStr.trim();
	
	    req.setAttribute("pageTitle", "Recovery Plan");
	    req.setAttribute("planActionUrl", req.getContextPath() + req.getServletPath());
	    req.setAttribute("activePage", "academic_recovery_plan");
	    req.setAttribute("q", keyword);
	
	    try {
	        StudentResultDAO resultDAO = new StudentResultDAO();
	        List<RecoveryCandidateRow> candidates;
	
	        if (keyword != null && !keyword.isBlank()) {
	            candidates = resultDAO.findRecoveryCandidatesByKeyword(keyword);
	        } else {
	            candidates = resultDAO.findAllRecoveryCandidates();
	        }
	
	        Map<String, List<RecoveryCandidateRow>> grouped = new LinkedHashMap<>();
	        for (RecoveryCandidateRow row : candidates) {
	            String key = row.getStudentId() + "||" + row.getStudentName();
	            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(row);
	        }
	
	        req.setAttribute("candidates", candidates);
	        req.setAttribute("groupedCandidates", grouped);
	
	        if (studentId != null && !studentId.isBlank()
	                && courseCode != null && !courseCode.isBlank()
	                && attemptNoStr != null && !attemptNoStr.isBlank()) {
	
	            int attemptNo = Integer.parseInt(attemptNoStr);
	
	            RecoveryPlan plan = recoveryPlanEJB.getPlan(studentId, courseCode, attemptNo);
	            List<Milestone> milestones = milestoneEJB.list(studentId, courseCode, attemptNo);
	            List<com.crs.entity.StudentResult> failedComponents =
	                    resultDAO.findFailedComponents(studentId, courseCode, attemptNo);
	
	            String selectedStudentName = null;
	            String selectedCourseName = null;
	
	            for (RecoveryCandidateRow row : candidates) {
	                if (studentId.equalsIgnoreCase(row.getStudentId())
	                        && courseCode.equalsIgnoreCase(row.getCourseCode())
	                        && attemptNo == row.getAttemptNo()) {
	                    selectedStudentName = row.getStudentName();
	                    selectedCourseName = row.getCourseName();
	                    break;
	                }
	            }
	
	            req.setAttribute("studentId", studentId);
	            req.setAttribute("studentName", selectedStudentName);
	            req.setAttribute("courseCode", courseCode);
	            req.setAttribute("courseName", selectedCourseName);
	            req.setAttribute("attemptNo", attemptNo);
	            req.setAttribute("plan", plan);
	            req.setAttribute("milestones", milestones);
	            req.setAttribute("failedComponents", failedComponents);
	        }
	
	    } catch (NumberFormatException e) {
	        req.setAttribute("error", "Invalid attempt number.");
	    } catch (Exception e) {
	        e.printStackTrace();
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

        if (action != null) action = action.trim();
        if (studentId != null) studentId = studentId.trim().toUpperCase();
        if (courseCode != null) courseCode = courseCode.trim().toUpperCase();
        if (attemptNoStr != null) attemptNoStr = attemptNoStr.trim();

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
            req.setAttribute("error", "Invalid attempt number.");
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
            e.printStackTrace();
            req.setAttribute("error", "Failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}