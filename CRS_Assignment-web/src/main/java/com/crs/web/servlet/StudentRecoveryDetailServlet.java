package com.crs.web.servlet;

import com.crs.dao.StudentResultDAO;
import com.crs.ejb.dto.RecoveryCandidateRow;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/academic/student_recovery_detail", "/admin/student_recovery_detail"})
public class StudentRecoveryDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");
        if (studentId != null) studentId = studentId.trim().toUpperCase();

        String basePath = req.getServletPath().startsWith("/admin") ? "/admin" : "/academic";

        req.setAttribute("pageTitle", "Student Recovery Detail");
        req.setAttribute("activePage", "academic_recovery_plan");
        req.setAttribute("basePath", basePath);

        if (studentId == null || studentId.isBlank()) {
            req.setAttribute("error", "student_id is required.");
            req.getRequestDispatcher("/academic/student_recovery_detail.jsp").forward(req, resp);
            return;
        }

        try {
            StudentResultDAO dao = new StudentResultDAO();
            List<RecoveryCandidateRow> courses = dao.findRecoveryCandidatesByStudent(studentId);

            req.setAttribute("studentId", studentId);
            req.setAttribute("courses", courses);

            if (!courses.isEmpty()) {
                req.setAttribute("studentName", courses.get(0).getStudentName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load student recovery detail: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/student_recovery_detail.jsp").forward(req, resp);
    }
}