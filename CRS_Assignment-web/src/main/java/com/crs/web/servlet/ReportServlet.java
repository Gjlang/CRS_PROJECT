package com.crs.web.servlet;

import com.crs.ejb.NotificationEJB;
import com.crs.ejb.PerformanceReportEJB;
import com.crs.ejb.dto.ReportData;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/academic/report")
public class ReportServlet extends HttpServlet {

    @EJB
    private PerformanceReportEJB performanceReportEJB;

    @EJB
    private NotificationEJB notificationEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/academic/report.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("student_id");
        int semester = Integer.parseInt(req.getParameter("semester"));
        int year = Integer.parseInt(req.getParameter("year"));
        int yos = Integer.parseInt(req.getParameter("year_of_study"));
        String emailTo = req.getParameter("email_to");

        try {
            ReportData data = performanceReportEJB.generateReport(studentId, semester, year, yos);
            req.setAttribute("report", data);

            if (emailTo != null && !emailTo.isBlank() && notificationEJB != null) {
                String body = "Report for " + data.getStudentName() + " (" + data.getStudentId() + ")\n"
                        + "Semester " + semester + ", Year " + year + ", Year of Study " + yos + "\n"
                        + "CGPA: " + String.format("%.2f", data.getCgpa());
                notificationEJB.sendReportEmail(emailTo.trim(), body);
                req.setAttribute("message", "Report emailed (check notifications table if SMTP not set).");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Failed: " + e.getMessage());
        }
        req.getRequestDispatcher("/academic/report.jsp").forward(req, resp);
    }
}

