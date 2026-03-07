package com.crs.web.servlet;
import com.crs.ejb.EnrolmentEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/enrolments")
public class AdminEnrolmentApprovalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private EnrolmentEJB enrolmentEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	req.setAttribute("activePage", "admin_enrolments");
        try {
            req.setAttribute("pending", enrolmentEJB.listPendingForAdmin());
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load pending enrolments: " + e.getMessage());
        }
        req.getRequestDispatcher("/admin/enrolments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action     = req.getParameter("action");
        String idParam    = req.getParameter("enrolment_id");
        String reason     = req.getParameter("reason");
        long adminId      = (long) req.getSession().getAttribute("userId");

        try {
            if (idParam == null || idParam.isBlank()) {
                throw new IllegalArgumentException("enrolment_id is required.");
            }
            long enrolmentId = Long.parseLong(idParam.trim());

            if ("approve".equalsIgnoreCase(action)) {
                enrolmentEJB.approve(enrolmentId, adminId);
                req.setAttribute("message", "Enrolment #" + enrolmentId + " approved successfully.");

            } else if ("reject".equalsIgnoreCase(action)) {
                enrolmentEJB.reject(enrolmentId, adminId, reason);
                req.setAttribute("message", "Enrolment #" + enrolmentId + " rejected.");

            } else {
                throw new IllegalArgumentException("Unknown action: " + action);
            }

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (IllegalStateException e) {
            // enrolment not found or already decided
            req.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            req.setAttribute("error", "Action failed: " + e.getMessage());
        }

        // Reload the pending list after action
        doGet(req, resp);
    }
}