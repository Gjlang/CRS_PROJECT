package com.crs.web.servlet;

import com.crs.ejb.EnrolmentEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/academic/enrolment")
public class AcademicEnrolmentServlet extends HttpServlet {

    @EJB
    private EnrolmentEJB enrolmentEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("pending", enrolmentEJB.listPending());
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load pending enrolments: " + e.getMessage());
        }
        req.getRequestDispatcher("/academic/enrolment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");
        String courseCode = req.getParameter("course_code");
        try {
            enrolmentEJB.createEnrolment(studentId, courseCode);
            req.setAttribute("message", "Enrolment created (PENDING).");
        } catch (Exception e) {
            req.setAttribute("error", "Create enrolment failed: " + e.getMessage());
        }
        doGet(req, resp);
    }
}

