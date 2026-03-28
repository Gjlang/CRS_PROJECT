package com.crs.web.servlet;

import com.crs.ejb.ProgressionRegistrationEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet({"/academic/enrolment", "/academic/enrolments"})
public class AcademicEnrolmentServlet extends HttpServlet {

    @EJB
    private ProgressionRegistrationEJB registrationEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("activePage", "academic_enrolments");

        try {
            Object userIdObj = req.getSession().getAttribute("userId");
            if (userIdObj == null) {
                throw new IllegalStateException("Session expired. Please login again.");
            }

            long userId = ((Number) userIdObj).longValue();
            req.setAttribute("records", registrationEJB.listMyRegistrations(userId));

            Object success = req.getSession().getAttribute("successMessage");
            if (success != null) {
                req.setAttribute("message", success.toString());
                req.getSession().removeAttribute("successMessage");
            }

        } catch (Exception e) {
            req.setAttribute("error", "Failed to load progression registrations: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/enrolment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");

        try {
            if (studentId == null || studentId.isBlank()) {
                throw new IllegalArgumentException("Student ID is required.");
            }

            Object userIdObj = req.getSession().getAttribute("userId");
            if (userIdObj == null) {
                throw new IllegalStateException("Session expired. Please login again.");
            }

            long userId = ((Number) userIdObj).longValue();
            long id = registrationEJB.createRegistration(studentId.trim(), userId);

            req.getSession().setAttribute(
                "successMessage",
                "Progression registration request submitted successfully. Request ID: " + id + ". Waiting for admin approval."
            );

            resp.sendRedirect(req.getContextPath() + "/academic/enrolment");

        } catch (Exception e) {
            req.setAttribute("activePage", "academic_enrolments");
            req.setAttribute("error", "Create progression registration failed: " + e.getMessage());

            try {
                Object userIdObj = req.getSession().getAttribute("userId");
                if (userIdObj != null) {
                    long userId = ((Number) userIdObj).longValue();
                    req.setAttribute("records", registrationEJB.listMyRegistrations(userId));
                }
            } catch (Exception ignored) {
            }

            req.getRequestDispatcher("/academic/enrolment.jsp").forward(req, resp);
        }
    }
}