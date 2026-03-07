package com.crs.web.servlet;

import com.crs.ejb.EligibilityEJB;
import com.crs.ejb.dto.EligibilityResult;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/academic/eligibility")
public class AcademicEligibilityServlet extends HttpServlet {

    @EJB
    private EligibilityEJB eligibilityEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");

        // 2.1 — input validation
        if (studentId == null || studentId.isBlank()) {
            req.setAttribute("error", "Student ID is required.");
            doGet(req, resp);
            return;
        }

        try {
            EligibilityResult result = eligibilityEJB.checkStudent(studentId.trim());
            req.setAttribute("result", result);
            req.setAttribute("studentId", studentId.trim()); // 2.1 — pass studentId to JSP
            req.setAttribute("activePage", "academic_eligibility");
            req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}