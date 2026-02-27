package com.crs.web.servlet;

import com.crs.ejb.RecoverySummaryReportEJB;
import com.crs.ejb.dto.RecoverySummaryRow;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/admin/recovery_summary")
public class AdminRecoverySummaryServlet extends HttpServlet {

    @EJB
    private RecoverySummaryReportEJB reportEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ Servlet-level RBAC (required even if Filter exists)
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

        String export = req.getParameter("export");
        boolean exportCsv = export != null && export.equalsIgnoreCase("csv");

        List<RecoverySummaryRow> rows = reportEJB.getRecoverySummary();

        if (exportCsv) {
            writeCsv(resp, rows);
            return;
        }

        // HTML mode
        req.setAttribute("rows", rows);
        req.getRequestDispatcher("/WEB-INF/views/admin/recovery_summary.jsp")
           .forward(req, resp);
    }

    private void writeCsv(HttpServletResponse resp, List<RecoverySummaryRow> rows) throws IOException {
        // ✅ Headers required
        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"recovery_summary.csv\"");

        // Optional BOM (helps Excel with UTF-8)
        resp.getOutputStream().write(new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF});

        try (PrintWriter out = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8)) {
            // Header row (exact required fields)
            out.println("student_id,course_code,attempt_count,eligibility_status,enrolment_status,reject_reason,recovery_plan_summary,milestone_count");

            for (RecoverySummaryRow r : rows) {
                out.print(csv(r.getStudentId())); out.print(",");
                out.print(csv(r.getCourseCode())); out.print(",");
                out.print(r.getAttemptCount()); out.print(",");
                out.print(csv(r.getEligibilityStatus())); out.print(",");
                out.print(csv(r.getEnrolmentStatus())); out.print(",");
                out.print(csv(nullToBlank(r.getRejectReason()))); out.print(",");
                out.print(csv(nullToBlank(r.getRecoveryPlanSummary()))); out.print(",");
                out.print(r.getMilestoneCount());
                out.println();
            }
            out.flush();
        }
    }

    private String nullToBlank(String s) {
        return (s == null) ? "" : s;
    }

    /**
     * Escapes commas/quotes/newlines properly for CSV:
     * - Wrap in quotes if needed
     * - Double quotes inside
     */
    private String csv(String value) {
        if (value == null) return "";
        boolean mustQuote = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        String escaped = value.replace("\"", "\"\"");
        return mustQuote ? ("\"" + escaped + "\"") : escaped;
    }
}