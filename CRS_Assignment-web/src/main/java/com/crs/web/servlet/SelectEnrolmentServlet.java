package com.crs.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/academic/select_enrolment")
public class SelectEnrolmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("enrolment_id");
        if (id == null || id.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
            return;
        }
        // Save selected enrolment into session so Recovery Plan & Milestones can use it
        req.getSession().setAttribute("selectedEnrolmentId", Long.parseLong(id));
        // Redirect back to wherever the user came from, default to enrolments
        String referer = req.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
        }
    }
}
