package com.crs.web.servlet;

import com.crs.dao.StudentDAO;
import com.crs.dao.StudentResultDAO;
import com.crs.ejb.NotificationEJB;
import com.crs.ejb.PerformanceReportEJB;
import com.crs.ejb.dto.ReportData;
import com.crs.ejb.dto.StudyContextOption;
import com.crs.entity.Student;
import com.crs.entity.StudentResult;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

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
            	        + "CGPA: " + String.format("%.2f", data.getCgpa()) + "\n"
            	        + "Weak / Failed Courses: " + data.getWeakCoursesSummary() + "\n"
            	        + "Recommendation: " + data.getImprovementRecommendation() + "\n"
            	        + "Suggested Recovery Action: " + data.getSuggestedRecoveryAction();

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
            List<StudyContextOption> contexts = resultDAO.findAvailableStudyContexts(studentId);

            int semester = (latest != null) ? latest.getSemester() : 1;
            int year = (latest != null) ? latest.getYear() : 2026;
            int yearOfStudy = (latest != null) ? latest.getYearOfStudy() : s.getYear();

            StringBuilder contextsJson = new StringBuilder("[");
            for (int i = 0; i < contexts.size(); i++) {
                StudyContextOption ctx = contexts.get(i);
                if (i > 0) {
                    contextsJson.append(",");
                }
                contextsJson.append("{")
                        .append("\"semester\":").append(ctx.getSemester()).append(",")
                        .append("\"year\":").append(ctx.getYear()).append(",")
                        .append("\"yearOfStudy\":").append(ctx.getYearOfStudy())
                        .append("}");
            }
            contextsJson.append("]");

            String json = "{"
                    + "\"found\":true,"
                    + "\"studentId\":\"" + escapeJson(s.getStudentId()) + "\","
                    + "\"studentName\":\"" + escapeJson(s.getFullName().trim()) + "\","
                    + "\"major\":\"" + escapeJson(s.getMajor()) + "\","
                    + "\"semester\":" + semester + ","
                    + "\"year\":" + year + ","
                    + "\"yearOfStudy\":" + yearOfStudy + ","
                    + "\"contexts\":" + contextsJson
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