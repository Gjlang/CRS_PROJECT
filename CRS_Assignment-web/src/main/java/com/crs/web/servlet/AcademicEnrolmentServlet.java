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
        String attemptNoStr = req.getParameter("attempt_no");

        try {
            if (attemptNoStr == null || attemptNoStr.isBlank()) {
                throw new IllegalArgumentException("Attempt No is required.");
            }

            int attemptNo = Integer.parseInt(attemptNoStr.trim());
            long userId = (long) req.getSession().getAttribute("userId");

            enrolmentEJB.createEnrolment(
                    studentId == null ? "" : studentId.trim(),
                    courseCode == null ? "" : courseCode.trim(),
                    attemptNo,
                    userId
            );

            resp.sendRedirect(req.getContextPath() + "/academic/enrolment");

        } catch (Exception e) {
            req.setAttribute("error", "Create enrolment failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}