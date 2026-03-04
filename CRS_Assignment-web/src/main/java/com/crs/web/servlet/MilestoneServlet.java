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

        String enrolmentIdStr = req.getParameter("enrolment_id");

        // Fallback to session if not in URL (e.g. clicked from sidebar)
        if (enrolmentIdStr == null || enrolmentIdStr.isBlank()) {
            Object selected = req.getSession().getAttribute("selectedEnrolmentId");
            if (selected != null) enrolmentIdStr = String.valueOf(selected);
        }

        // Still nothing — send to enrolments to pick one
        if (enrolmentIdStr == null || enrolmentIdStr.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
            return;
        }

        try {
            long enrolmentId = Long.parseLong(enrolmentIdStr);
            List<Milestone> milestones = milestoneEJB.list(enrolmentId);

            req.setAttribute("enrolmentId", enrolmentId);
            req.setAttribute("milestones", milestones);
            req.setAttribute("activePage", "academic_milestones");

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid enrolment_id");
            return;
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load milestones: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/milestones.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // All milestone write actions go through RecoveryPlanServlet
        resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
    }
}
