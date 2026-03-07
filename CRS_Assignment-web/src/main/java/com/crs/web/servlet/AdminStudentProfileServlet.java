package com.crs.web.servlet;

import com.crs.ejb.AdminStudentProfileEJB;
import com.crs.ejb.dto.AdminStudentProfileData;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/student_profile")
public class AdminStudentProfileServlet extends HttpServlet {

    @EJB
    private AdminStudentProfileEJB adminStudentProfileEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"COURSE_ADMIN".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String studentId = req.getParameter("student_id");
        if (studentId == null || studentId.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "student_id is required");
            return;
        }

        try {
            AdminStudentProfileData profile = adminStudentProfileEJB.getStudentProfile(studentId.trim());

            if (profile == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
                return;
            }

            req.setAttribute("profile", profile);
            req.setAttribute("activePage", "admin_students");
            req.getRequestDispatcher("/admin/student_profile.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", "Failed to load student profile: " + e.getMessage());
            req.setAttribute("activePage", "admin_students");
            req.getRequestDispatcher("/admin/student_profile.jsp").forward(req, resp);
        }
    }
}