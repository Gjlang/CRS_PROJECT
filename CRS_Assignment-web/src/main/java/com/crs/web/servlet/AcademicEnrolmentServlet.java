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
            // 5.1 doGet — show only this academic officer's own requests
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
            // 5.1 doPost — pass userId so request is tracked to this academic officer
            long userId = (long) req.getSession().getAttribute("userId");
            enrolmentEJB.createEnrolment(studentId, courseCode, userId);
            req.setAttribute("message", "Enrolment request created (PENDING).");
        } catch (IllegalStateException e) {
            // Catches eligibility FAIL and already-decided errors with clear message
            req.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            req.setAttribute("error", "Create enrolment failed: " + e.getMessage());
        }
        doGet(req, resp);
    }
}