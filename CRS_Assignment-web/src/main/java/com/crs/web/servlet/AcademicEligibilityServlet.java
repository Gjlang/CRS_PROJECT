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
        try {
            EligibilityResult r = eligibilityEJB.checkStudent(studentId);
            req.setAttribute("result", r);
        } catch (Exception e) {
            req.setAttribute("error", "Failed: " + e.getMessage());
        }
        req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
    }
}

