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
        req.setAttribute("activePage", "admin_enrolments");

        try {
            req.setAttribute("registrations", progressionRegistrationEJB.listAllRegistrations());
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load progression registrations: " + e.getMessage());
        }

        req.getRequestDispatcher("/admin/enrolments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
