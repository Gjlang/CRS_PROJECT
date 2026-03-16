package com.crs.web.servlet;

import com.crs.dao.StudentResultDAO;
import com.crs.ejb.EligibilityEJB;
import com.crs.ejb.dto.AdminStudentResultRow;
import com.crs.ejb.dto.EligibilityResult;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/academic/eligibility")
public class AcademicEligibilityServlet extends HttpServlet {

    @EJB
    private EligibilityEJB eligibilityEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<EligibilityResult> notEligibleList = eligibilityEJB.listAllNotEligibleStudents();
            req.setAttribute("notEligibleList", notEligibleList);
            req.setAttribute("activePage", "academic_eligibility");
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load eligibility list: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");

        try {
            List<EligibilityResult> notEligibleList = eligibilityEJB.listAllNotEligibleStudents();
            req.setAttribute("notEligibleList", notEligibleList);

            if (studentId == null || studentId.isBlank()) {
                req.setAttribute("error", "Student ID is required.");
                req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);
                return;
            }

            EligibilityResult singleResult = eligibilityEJB.checkStudent(studentId.trim());
            req.setAttribute("result", singleResult);
            req.setAttribute("studentId", studentId.trim());
            req.setAttribute("activePage", "academic_eligibility");

            try {
                List<AdminStudentResultRow> detailedResults =
                        new StudentResultDAO().findDetailedResultsByStudent(studentId.trim());
                req.setAttribute("detailedResults", detailedResults);
            } catch (Exception e) {
                req.setAttribute("detailedResults", java.util.List.of());
            }

            req.getRequestDispatcher("/academic/eligibility.jsp").forward(req, resp);

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Eligibility check failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
