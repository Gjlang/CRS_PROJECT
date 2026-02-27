package com.crs.dao;

import com.crs.ejb.dto.RecoverySummaryRow;
import com.crs.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecoverySummaryReportDAO {

    public List<RecoverySummaryRow> fetchRecoverySummary() throws SQLException {
        String sql =
            "SELECT " +
            "  e.student_id, " +
            "  e.course_code, " +
            "  ac.attempt_count, " +
            "  e.eligibility_status, " +
            "  e.enrolment_status, " +
            "  COALESCE(e.reject_reason, '') AS reject_reason, " +
            "  COALESCE(rp.recommendation, '') AS recovery_plan_summary, " +
            "  COALESCE(COUNT(m.milestone_id), 0) AS milestone_count " +
            "FROM enrolments e " +
            "JOIN ( " +
            "   SELECT student_id, course_code, MAX(enrolment_id) AS latest_enrolment_id, COUNT(*) AS attempt_count " +
            "   FROM enrolments " +
            "   GROUP BY student_id, course_code " +
            ") ac ON ac.latest_enrolment_id = e.enrolment_id " +
            "LEFT JOIN recovery_plans rp ON rp.enrolment_id = e.enrolment_id " +
            "LEFT JOIN milestones m ON m.plan_id = rp.plan_id " +
            "GROUP BY " +
            "  e.student_id, e.course_code, ac.attempt_count, " +
            "  e.eligibility_status, e.enrolment_status, e.reject_reason, rp.recommendation " +
            "ORDER BY e.student_id ASC, e.course_code ASC";

        List<RecoverySummaryRow> rows = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String studentId = rs.getString("student_id");
                String courseCode = rs.getString("course_code");
                int attemptCount = rs.getInt("attempt_count");
                String eligibilityStatus = rs.getString("eligibility_status");
                String enrolmentStatus = rs.getString("enrolment_status");
                String rejectReason = rs.getString("reject_reason");
                String planSummary = rs.getString("recovery_plan_summary");
                int milestoneCount = rs.getInt("milestone_count");

                rows.add(new RecoverySummaryRow(
                        studentId,
                        courseCode,
                        attemptCount,
                        eligibilityStatus,
                        enrolmentStatus,
                        rejectReason,
                        planSummary,
                        milestoneCount
                ));
            }
        }

        return rows;
    }
}