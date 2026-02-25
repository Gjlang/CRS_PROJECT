package com.crs.web.servlet;

import com.crs.ejb.EnrolmentEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet({"/academic/enrolment", "/academic/enrolments"})
public class AcademicEnrolmentServlet extends HttpServlet {

    @EJB
    private EnrolmentEJB enrolmentEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long userId = (long) req.getSession().getAttribute("userId");
            req.setAttribute("requests", enrolmentEJB.listMyRequests(userId));
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load enrolment requests: " + e.getMessage());
        }
        req.getRequestDispatcher("/academic/enrolment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");
        String courseCode = req.getParameter("course_code");

        try {
            long userId = (long) req.getSession().getAttribute("userId");
            enrolmentEJB.createEnrolment(
                studentId == null ? "" : studentId.trim(),
                courseCode == null ? "" : courseCode.trim(),
                userId);
            // Redirect on success to prevent form resubmit on refresh
            resp.sendRedirect(req.getContextPath() + "/academic/enrolment");
        } catch (IllegalStateException e) {
            // Eligibility FAIL, max attempts, already decided — show clean message
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Create enrolment failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
