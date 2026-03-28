package com.crs.web.servlet;

import com.crs.ejb.ProgressionRegistrationEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/enrolments")
public class AdminEnrolmentApprovalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private ProgressionRegistrationEJB progressionRegistrationEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        loadPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("activePage", "admin_enrolments");

        String action = req.getParameter("action");
        String registrationIdStr = req.getParameter("registration_id");

        try {
            if (registrationIdStr == null || registrationIdStr.isBlank()) {
                throw new IllegalArgumentException("Registration ID is required.");
            }

            long registrationId = Long.parseLong(registrationIdStr);

            Object userIdObj = req.getSession().getAttribute("userId");
            if (userIdObj == null) {
                throw new IllegalStateException("Session expired. Please login again.");
            }

            long adminUserId = ((Number) userIdObj).longValue();

            if ("approve".equalsIgnoreCase(action)) {
                progressionRegistrationEJB.approveRegistration(registrationId, adminUserId);
                req.getSession().setAttribute("successMessage", "Registration ID " + registrationId + " approved successfully.");
            } else if ("reject".equalsIgnoreCase(action)) {
                String reason = req.getParameter("reject_reason");
                progressionRegistrationEJB.rejectRegistration(registrationId, adminUserId, reason);
                req.getSession().setAttribute("successMessage", "Registration ID " + registrationId + " rejected successfully.");
            } else {
                throw new IllegalArgumentException("Invalid action.");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/enrolments");

        } catch (Exception e) {
            req.setAttribute("error", "Failed to process registration approval: " + e.getMessage());
            loadPage(req, resp);
        }
    }

    private void loadPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("activePage", "admin_enrolments");

        try {
            Object success = req.getSession().getAttribute("successMessage");
            if (success != null) {
                req.setAttribute("message", success.toString());
                req.getSession().removeAttribute("successMessage");
            }

            req.setAttribute("registrations", progressionRegistrationEJB.listAllRegistrations());
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load progression registrations: " + e.getMessage());
        }

        req.getRequestDispatcher("/admin/enrolments.jsp").forward(req, resp);
    }
}