package com.crs.web.servlet;

import com.crs.ejb.EnrolmentEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet({"/academic/enrolment", "/academic/enrolments"})
public class AcademicEnrolmentServlet extends HttpServlet {

    @EJB
    private EnrolmentEJB enrolmentEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object userIdObj = req.getSession().getAttribute("userId");
            if (userIdObj == null) {
                throw new IllegalStateException("Session expired. Please login again.");
            }

            long userId = ((Number) userIdObj).longValue();
            req.setAttribute("records", enrolmentEJB.listMyRequests(userId));

            Object success = req.getSession().getAttribute("successMessage");
            if (success != null) {
                req.setAttribute("message", success.toString());
                req.getSession().removeAttribute("successMessage");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Failed to load enrolment requests: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/enrolment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");
        String courseCode = req.getParameter("course_code");
        String attemptNoStr = req.getParameter("attempt_no");

        try {
            if (studentId == null || studentId.isBlank()) {
                throw new IllegalArgumentException("Student ID is required.");
            }
            if (courseCode == null || courseCode.isBlank()) {
                throw new IllegalArgumentException("Course Code is required.");
            }
            if (attemptNoStr == null || attemptNoStr.isBlank()) {
                throw new IllegalArgumentException("Attempt No is required.");
            }

            int attemptNo = Integer.parseInt(attemptNoStr.trim());

            Object userIdObj = req.getSession().getAttribute("userId");
            if (userIdObj == null) {
                throw new IllegalStateException("Session expired. Please login again.");
            }

            long userId = ((Number) userIdObj).longValue();

            long enrolmentId = enrolmentEJB.createProgressionEnrolment(
                    studentId.trim(),
                    courseCode.trim(),
                    attemptNo,
                    userId
            );

            req.getSession().setAttribute(
                    "successMessage",
                    "Enrolment record created successfully. ID: " + enrolmentId
            );

            resp.sendRedirect(req.getContextPath() + "/academic/enrolment");

        } catch (Exception e) {
            req.setAttribute("error", "Create enrolment failed: " + e.getMessage());

            try {
                Object userIdObj = req.getSession().getAttribute("userId");
                if (userIdObj != null) {
                    long userId = ((Number) userIdObj).longValue();
                    req.setAttribute("records", enrolmentEJB.listMyRequests(userId));
                }
            } catch (Exception ignored) {
            }

            req.getRequestDispatcher("/academic/enrolment.jsp").forward(req, resp);
        }
    }
}
