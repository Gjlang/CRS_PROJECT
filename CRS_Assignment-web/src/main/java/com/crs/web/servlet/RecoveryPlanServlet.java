package com.crs.web.servlet;

import com.crs.dao.StudentResultDAO;
import com.crs.ejb.dto.StudentRecoverySummaryRow;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/academic/recovery_plan", "/admin/recovery_plan"})
public class RecoveryPlanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("q");
        if (keyword != null) keyword = keyword.trim();

        String basePath = req.getServletPath().startsWith("/admin") ? "/admin" : "/academic";

        req.setAttribute("pageTitle", "Recovery Plan");
        req.setAttribute("activePage", "academic_recovery_plan");
        req.setAttribute("q", keyword);
        req.setAttribute("basePath", basePath);

        try {
            StudentResultDAO resultDAO = new StudentResultDAO();
            List<StudentRecoverySummaryRow> students;

            if (keyword != null && !keyword.isBlank()) {
                students = resultDAO.findStudentRecoverySummariesByKeyword(keyword);
            } else {
                students = resultDAO.findStudentRecoverySummaries();
            }

            req.setAttribute("students", students);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load recovery candidates: " + e.getMessage());
        }

        req.getRequestDispatcher("/academic/recovery_plan.jsp").forward(req, resp);
    }
}