package com.crs.web.servlet;

import com.crs.ejb.MilestoneEJB;
import com.crs.entity.Milestone;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/academic/milestones")
public class MilestoneServlet extends HttpServlet {

    @EJB
    private MilestoneEJB milestoneEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String studentId = req.getParameter("student_id");
        String courseCode = req.getParameter("course_code");
        String attemptNoStr = req.getParameter("attempt_no");

        if (studentId == null || studentId.isBlank()
                || courseCode == null || courseCode.isBlank()
                || attemptNoStr == null || attemptNoStr.isBlank()) {
            req.setAttribute("error", "student_id, course_code, and attempt_no are required.");
            req.getRequestDispatcher("/academic/milestones.jsp").forward(req, resp);
            return;
        }

        try {
            int attemptNo = Integer.parseInt(attemptNoStr);

            List<Milestone> milestones = milestoneEJB.list(studentId, courseCode, attemptNo);

            req.setAttribute("studentId", studentId);
            req.setAttribute("courseCode", courseCode);
            req.setAttribute("attemptNo", attemptNo);
            req.setAttribute("milestones", milestones);
            req.setAttribute("activePage", "academic_milestones");

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid attempt_no.");
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load milestones: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/milestones.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // All milestone write actions go through RecoveryPlanServlet
        String studentId = req.getParameter("student_id");
        String courseCode = req.getParameter("course_code");
        String attemptNo = req.getParameter("attempt_no");

        if (studentId != null && courseCode != null && attemptNo != null) {
            resp.sendRedirect(req.getContextPath() + "/academic/recovery_plan"
                    + "?student_id=" + studentId
                    + "&course_code=" + courseCode
                    + "&attempt_no=" + attemptNo);
        } else {
            resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
        }
    }
}