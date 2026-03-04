package com.crs.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/academic/dashboard")
public class AcademicDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("activePage", "academic_dashboard");
        req.getRequestDispatcher("/academic/dashboard.jsp").forward(req, resp);
    }
}
