package com.crs.web.servlet;

import com.crs.ejb.AdminStudentProfileEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/students")
public class AdminStudentsServlet extends HttpServlet {

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

        try {
            req.setAttribute("students", adminStudentProfileEJB.listAllStudents());
            req.setAttribute("activePage", "admin_students");
            req.getRequestDispatcher("/admin/students.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load students: " + e.getMessage());
            req.setAttribute("activePage", "admin_students");
            req.getRequestDispatcher("/admin/students.jsp").forward(req, resp);
        }
    }
}