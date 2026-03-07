package com.crs.dao;

import com.crs.ejb.dto.AdminStudentEnrolmentRow;
import com.crs.ejb.dto.AdminStudentListRow;
import com.crs.ejb.dto.AdminStudentMilestoneRow;
import com.crs.ejb.dto.AdminStudentProfileData;
import com.crs.ejb.dto.AdminStudentRecoveryPlanRow;
import com.crs.ejb.dto.AdminStudentResultRow;
import com.crs.util.DbUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AdminStudentProfileDAO {

    public List<AdminStudentListRow> listAllStudents() throws SQLException {
        String sql = """
            SELECT
                s.StudentID,
                s.FirstName,
                s.LastName,
                s.Major,
                s.Year,
                s.Email,
                s.active
            FROM students s
            ORDER BY s.StudentID
            """;

        List<AdminStudentListRow> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AdminStudentListRow row = new AdminStudentListRow();
                row.setStudentId(rs.getString("StudentID"));
                row.setFullName(buildFullName(
                        rs.getString("FirstName"),
                        rs.getString("LastName")
                ));
                row.setMajor(rs.getString("Major"));
                row.setYear(rs.getInt("Year"));
                row.setEmail(rs.getString("Email"));
                row.setActive(rs.getBoolean("active"));
                list.add(row);
            }
        }

        return list;
    }

    public AdminStudentProfileData findStudentProfile(String studentId) throws SQLException {
        AdminStudentProfileData data = findStudentDetails(studentId);
        if (data == null) {
            return null;
        }

        data.setEnrolments(findEnrolments(studentId));
        data.setResults(findResults(studentId));
        data.setRecoveryPlans(findRecoveryPlans(studentId));
        data.setMilestones(findMilestones(studentId));

        return data;
    }

    private AdminStudentProfileData findStudentDetails(String studentId) throws SQLException {
        String sql = """
            SELECT
                s.StudentID,
                s.FirstName,
                s.LastName,
                s.Major,
                s.Year,
                s.Email,
                s.active
            FROM students s
            WHERE s.StudentID = ?
            """;

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                AdminStudentProfileData data = new AdminStudentProfileData();
                data.setStudentId(rs.getString("StudentID"));
                data.setFirstName(rs.getString("FirstName"));
                data.setLastName(rs.getString("LastName"));
                data.setMajor(rs.getString("Major"));
                data.setYear(rs.getInt("Year"));
                data.setEmail(rs.getString("Email"));
                data.setActive(rs.getBoolean("active"));
                return data;
            }
        }
    }

    private List<AdminStudentEnrolmentRow> findEnrolments(String studentId) throws SQLException {
        String sql = """
            SELECT
                e.enrolment_id,
                e.course_code,
                c.CourseName,
                e.attempt_no,
                e.eligibility_status,
                e.enrolment_status,
                e.created_at,
                e.reject_reason
            FROM enrolments e
            LEFT JOIN courses c
                   ON c.CourseID = e.course_code
            WHERE e.student_id = ?
            ORDER BY e.created_at DESC, e.enrolment_id DESC
            """;

        List<AdminStudentEnrolmentRow> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdminStudentEnrolmentRow row = new AdminStudentEnrolmentRow();
                    row.setEnrolmentId(rs.getLong("enrolment_id"));
                    row.setCourseCode(rs.getString("course_code"));
                    row.setCourseName(rs.getString("CourseName"));
                    row.setAttemptNo(rs.getInt("attempt_no"));
                    row.setEligibilityStatus(rs.getString("eligibility_status"));
                    row.setEnrolmentStatus(rs.getString("enrolment_status"));

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        row.setCreatedAt(ts.toLocalDateTime());
                    }

                    row.setRejectReason(rs.getString("reject_reason"));
                    list.add(row);
                }
            }
        }

        return list;
    }

    private List<AdminStudentResultRow> findResults(String studentId) throws SQLException {
        String sql = """
            SELECT
                r.result_id,
                r.course_code,
                c.CourseName,
                a.component_name,
                r.grade,
                r.grade_point,
                r.failed,
                r.attempt_no,
                r.semester,
                r.year,
                r.year_of_study
            FROM student_results r
            LEFT JOIN assessments a
                   ON a.assessment_id = r.assessment_id
            LEFT JOIN courses c
                   ON c.CourseID = r.course_code
            WHERE r.student_id = ?
            ORDER BY r.year DESC, r.semester DESC, r.course_code, r.attempt_no, a.component_name
            """;

        List<AdminStudentResultRow> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdminStudentResultRow row = new AdminStudentResultRow();
                    row.setResultId(rs.getLong("result_id"));
                    row.setCourseCode(rs.getString("course_code"));
                    row.setCourseName(rs.getString("CourseName"));
                    row.setAssessmentComponent(rs.getString("component_name"));
                    row.setGrade(rs.getString("grade"));
                    row.setGradePoint(rs.getDouble("grade_point"));
                    row.setFailed(rs.getBoolean("failed"));
                    row.setAttemptNo(rs.getInt("attempt_no"));
                    row.setSemester(rs.getInt("semester"));
                    row.setYear(rs.getInt("year"));
                    row.setYearOfStudy(rs.getInt("year_of_study"));
                    list.add(row);
                }
            }
        }

        return list;
    }

    private List<AdminStudentRecoveryPlanRow> findRecoveryPlans(String studentId) throws SQLException {
        String sql = """
            SELECT
                rp.plan_id,
                rp.enrolment_id,
                rp.course_code,
                c.CourseName,
                rp.attempt_no,
                rp.recommendation,
                rp.created_at,
                COUNT(m.milestone_id) AS milestone_count
            FROM recovery_plans rp
            LEFT JOIN courses c
                   ON c.CourseID = rp.course_code
            LEFT JOIN milestones m
                   ON m.plan_id = rp.plan_id
            WHERE rp.student_id = ?
            GROUP BY
                rp.plan_id,
                rp.enrolment_id,
                rp.course_code,
                c.CourseName,
                rp.attempt_no,
                rp.recommendation,
                rp.created_at
            ORDER BY rp.created_at DESC, rp.plan_id DESC
            """;

        List<AdminStudentRecoveryPlanRow> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdminStudentRecoveryPlanRow row = new AdminStudentRecoveryPlanRow();
                    row.setPlanId(rs.getLong("plan_id"));
                    row.setEnrolmentId((Long) rs.getObject("enrolment_id"));
                    row.setCourseCode(rs.getString("course_code"));
                    row.setCourseName(rs.getString("CourseName"));
                    row.setAttemptNo(rs.getInt("attempt_no"));
                    row.setRecommendation(rs.getString("recommendation"));

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        row.setCreatedAt(ts.toLocalDateTime());
                    }

                    row.setMilestoneCount(rs.getLong("milestone_count"));
                    list.add(row);
                }
            }
        }

        return list;
    }

    private List<AdminStudentMilestoneRow> findMilestones(String studentId) throws SQLException {
        String sql = """
            SELECT
                m.milestone_id,
                m.plan_id,
                rp.course_code,
                c.CourseName,
                m.study_week,
                m.title,
                m.task,
                m.due_date,
                m.status,
                m.grade,
                m.remarks
            FROM milestones m
            INNER JOIN recovery_plans rp
                    ON rp.plan_id = m.plan_id
            LEFT JOIN courses c
                   ON c.CourseID = rp.course_code
            WHERE rp.student_id = ?
            ORDER BY m.due_date ASC, m.milestone_id ASC
            """;

        List<AdminStudentMilestoneRow> list = new ArrayList<>();

        try (Connection con = DbUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdminStudentMilestoneRow row = new AdminStudentMilestoneRow();
                    row.setMilestoneId(rs.getLong("milestone_id"));
                    row.setPlanId(rs.getLong("plan_id"));
                    row.setCourseCode(rs.getString("course_code"));
                    row.setCourseName(rs.getString("CourseName"));
                    row.setStudyWeek(rs.getInt("study_week"));
                    row.setTitle(rs.getString("title"));
                    row.setTask(rs.getString("task"));

                    Date due = rs.getDate("due_date");
                    if (due != null) {
                        row.setDueDate(due.toLocalDate());
                    }

                    row.setStatus(rs.getString("status"));
                    row.setGrade(rs.getString("grade"));
                    row.setRemarks(rs.getString("remarks"));
                    list.add(row);
                }
            }
        }

        return list;
    }

    private String buildFullName(String firstName, String lastName) {
        String fn = firstName == null ? "" : firstName.trim();
        String ln = lastName == null ? "" : lastName.trim();
        return (fn + " " + ln).trim();
    }
}