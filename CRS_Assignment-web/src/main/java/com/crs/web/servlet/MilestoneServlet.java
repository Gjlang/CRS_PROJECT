package com.crs.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/academic/milestones")
public class MilestoneServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Milestones are managed inside Recovery Plan (enrolment-based flow)
        resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // If someone posts here by mistake, redirect to the correct flow.
        resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
    }
}