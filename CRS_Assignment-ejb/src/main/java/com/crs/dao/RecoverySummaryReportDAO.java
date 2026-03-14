package com.crs.dao;

import com.crs.ejb.dto.RecoverySummaryRow;
import com.crs.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecoverySummaryReportDAO {

    public List<RecoverySummaryRow> findAll() throws SQLException {
        String sql = """
            SELECT
                e.student_id,
                e.course_code,
                COUNT(*) AS attempt_count,
                MAX(e.eligibility_status) AS eligibility_status,
                MAX(e.enrolment_status) AS enrolment_status,
                MAX(e.reject_reason) AS reject_reason,
                MAX(rp.recommendation) AS recovery_plan_summary,
                COUNT(DISTINCT m.milestone_id) AS milestone_count
            FROM enrolments e
            LEFT JOIN recovery_plans rp
                   ON rp.student_id = e.student_id
                  AND rp.course_code = e.course_code
                  AND rp.attempt_no = e.attempt_no
            LEFT JOIN milestones m
                   ON m.plan_id = rp.plan_id
            GROUP BY e.student_id, e.course_code
            ORDER BY e.student_id, e.course_code
            """;

        List<RecoverySummaryRow> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RecoverySummaryRow row = new RecoverySummaryRow();
                row.setStudentId(rs.getString("student_id"));
                row.setCourseCode(rs.getString("course_code"));
                row.setAttemptCount(rs.getInt("attempt_count"));
                row.setEligibilityStatus(rs.getString("eligibility_status"));
                row.setEnrolmentStatus(rs.getString("enrolment_status"));
                row.setRejectReason(rs.getString("reject_reason"));
                row.setRecoveryPlanSummary(rs.getString("recovery_plan_summary"));
                row.setMilestoneCount(rs.getInt("milestone_count"));
                list.add(row);
            }
        }

        return list;
    }

    public List<RecoverySummaryRow> fetchRecoverySummary() throws SQLException {
        return findAll();
    }
}
