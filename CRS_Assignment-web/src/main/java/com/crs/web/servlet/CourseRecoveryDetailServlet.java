package com.crs.web.servlet;

import com.crs.dao.StudentResultDAO;
import com.crs.ejb.MilestoneEJB;
import com.crs.ejb.RecoveryPlanEJB;
import com.crs.ejb.dto.RecoveryCandidateRow;
import com.crs.entity.Milestone;
import com.crs.entity.RecoveryPlan;
import com.crs.entity.StudentResult;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(urlPatterns = {"/academic/course_recovery_detail", "/admin/course_recovery_detail"})
public class CourseRecoveryDetailServlet extends HttpServlet {

    @EJB
    private RecoveryPlanEJB recoveryPlanEJB;

    @EJB
    private MilestoneEJB milestoneEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");
        String courseCode = req.getParameter("course_code");
        String attemptNoStr = req.getParameter("attempt_no");

        if (studentId != null) studentId = studentId.trim().toUpperCase();
        if (courseCode != null) courseCode = courseCode.trim().toUpperCase();
        if (attemptNoStr != null) attemptNoStr = attemptNoStr.trim();

        String basePath = req.getServletPath().startsWith("/admin") ? "/admin" : "/academic";

        req.setAttribute("pageTitle", "Course Recovery Detail");
        req.setAttribute("activePage", "academic_recovery_plan");
        req.setAttribute("basePath", basePath);
        req.setAttribute("planActionUrl", req.getContextPath() + req.getServletPath());

        if (studentId == null || studentId.isBlank()
                || courseCode == null || courseCode.isBlank()
                || attemptNoStr == null || attemptNoStr.isBlank()) {
            req.setAttribute("error", "student_id, course_code, and attempt_no are required.");
            req.getRequestDispatcher("/academic/course_recovery_detail.jsp").forward(req, resp);
            return;
        }

        try {
            int attemptNo = Integer.parseInt(attemptNoStr);

            StudentResultDAO dao = new StudentResultDAO();
            List<StudentResult> failedComponents = dao.findFailedComponents(studentId, courseCode, attemptNo);
            List<RecoveryCandidateRow> studentCourses = dao.findRecoveryCandidatesByStudent(studentId);

            RecoveryPlan plan = recoveryPlanEJB.getPlan(studentId, courseCode, attemptNo);
            List<Milestone> milestones = milestoneEJB.list(studentId, courseCode, attemptNo);

            String studentName = null;
            String courseName = null;

            for (RecoveryCandidateRow row : studentCourses) {
                if (studentId.equalsIgnoreCase(row.getStudentId())
                        && courseCode.equalsIgnoreCase(row.getCourseCode())
                        && attemptNo == row.getAttemptNo()) {
                    studentName = row.getStudentName();
                    courseName = row.getCourseName();
                    break;
                }
            }

            req.setAttribute("studentId", studentId);
            req.setAttribute("studentName", studentName);
            req.setAttribute("courseCode", courseCode);
            req.setAttribute("courseName", courseName);
            req.setAttribute("attemptNo", attemptNo);
            req.setAttribute("failedComponents", failedComponents);
            req.setAttribute("plan", plan);
            req.setAttribute("milestones", milestones);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load course recovery detail: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/course_recovery_detail.jsp").forward(req, resp);
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

        long userId = 0L;
        Object userIdObj = req.getSession().getAttribute("userId");
        if (userIdObj != null) {
            if (userIdObj instanceof Long) {
                userId = (Long) userIdObj;
            } else {
                userId = Long.parseLong(String.valueOf(userIdObj));
            }
        }

        try {
            int attemptNo = Integer.parseInt(attemptNoStr);

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

            String basePath = req.getServletPath().startsWith("/admin") ? "/admin" : "/academic";

            resp.sendRedirect(req.getContextPath() + basePath + "/course_recovery_detail?student_id="
                    + studentId + "&course_code=" + courseCode + "&attempt_no=" + attemptNo);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}