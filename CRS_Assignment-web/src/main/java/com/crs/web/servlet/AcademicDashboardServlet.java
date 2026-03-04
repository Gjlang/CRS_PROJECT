package com.crs.web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/academic/dashboard")
public class AcademicDashboardServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.sendRedirect(req.getContextPath() + "/academic/enrolments");
  }
}