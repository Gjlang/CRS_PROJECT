package com.crs.web.servlet;

import com.crs.dao.StudentDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.ejb.NotificationEJB;
import com.crs.ejb.PerformanceReportEJB;
import com.crs.ejb.dto.ReportData;
import com.crs.entity.Student;
import com.crs.entity.StudentResult;
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
        String lookupStudentId = req.getParameter("lookup_student_id");
        if (lookupStudentId != null && !lookupStudentId.isBlank()) {
            handleStudentLookup(lookupStudentId.trim(), resp);
            return;
        }

        populateFormOptions(req);
        req.setAttribute("activePage", "report");
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
                        + "Major: " + data.getMajor() + "\n"
                        + "Semester " + semester + ", Year " + year + ", Year of Study " + yos + "\n"
                        + "CGPA: " + String.format("%.2f", data.getCgpa());

                notificationEJB.sendReportEmail(emailTo.trim(), body);
                req.setAttribute("message", "Report emailed (check notifications table if SMTP not set).");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Failed: " + e.getMessage());
        }

        populateFormOptions(req);
        req.setAttribute("activePage", "report");
        req.getRequestDispatcher("/academic/report.jsp").forward(req, resp);
    }

    private void populateFormOptions(HttpServletRequest req) {
        try {
            req.setAttribute("studentOptions", new StudentDAO().findAllActive());
        } catch (Exception e) {
            req.setAttribute("studentOptions", java.util.List.of());
        }
    }

    private void handleStudentLookup(String studentId, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        try {
            StudentDAO studentDAO = new StudentDAO();
            StudentResultDAO resultDAO = new StudentResultDAO();

            Student s = studentDAO.findById(studentId);
            if (s == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"found\":false}");
                return;
            }

            StudentResult latest = resultDAO.findLatestStudyContext(studentId);

            int semester = (latest != null) ? latest.getSemester() : 1;
            int year = (latest != null) ? latest.getYear() : 2026;
            int yearOfStudy = (latest != null) ? latest.getYearOfStudy() : s.getYear();

            String json = "{"
                    + "\"found\":true,"
                    + "\"studentId\":\"" + escapeJson(s.getStudentId()) + "\","
                    + "\"studentName\":\"" + escapeJson(s.getFullName().trim()) + "\","
                    + "\"major\":\"" + escapeJson(s.getMajor()) + "\","
                    + "\"semester\":" + semester + ","
                    + "\"year\":" + year + ","
                    + "\"yearOfStudy\":" + yearOfStudy
                    + "}";

            resp.getWriter().write(json);

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"found\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}