package com.crs.ejb;

import com.crs.dao.NotificationDAO;
import com.crs.entity.Notification;
import com.crs.util.EmailUtil;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.Session;

import java.sql.SQLException;
import java.util.List;

@Stateless
public class NotificationEJB {

    @Resource(lookup = "mail/crsMailSession")
    private Session mailSession;

    public void sendUserAccountEmail(String to, String name, String role) throws SQLException {
        String subject = "CRS Account Created";
        String body = "Hello " + safe(name) + ",\n\n"
                + "Your CRS account has been created.\n"
                + "Role: " + safe(role) + "\n\n"
                + "- CRS System";
        sendAndLog(to, subject, body, "USER_MGMT");
    }

    public void sendUserUpdatedEmail(String to, String name, String role, boolean active) throws SQLException {
        String subject = "CRS Account Updated";
        String body = "Hello " + safe(name) + ",\n\n"
                + "Your CRS account details have been updated.\n"
                + "Role: " + safe(role) + "\n"
                + "Active: " + (active ? "Yes" : "No") + "\n\n"
                + "- CRS System";
        sendAndLog(to, subject, body, "USER_MGMT");
    }

    public void sendUserDeactivatedEmail(String to, String name) throws SQLException {
        String subject = "CRS Account Deactivated";
        String body = "Hello " + safe(name) + ",\n\n"
                + "Your CRS account has been deactivated.\n\n"
                + "- CRS System";
        sendAndLog(to, subject, body, "USER_MGMT");
    }

    public void sendPasswordResetEmail(String to, String token) throws SQLException {
        String subject = "CRS Password Reset";
        String body = "Use this reset token to reset your password:\n\n"
                + token
                + "\n\nIf you did not request this, please ignore this email.";
        sendAndLog(to, subject, body, "PASSWORD_RESET");
    }

    public void sendRecoveryPlanEmail(String to, String studentId, String courseCode, int attemptNo, String recommendation)
            throws SQLException {
        String subject = "CRS Recovery Plan Updated";
        String body = "Recovery plan details:\n\n"
                + "Student ID: " + safe(studentId) + "\n"
                + "Course Code: " + safe(courseCode) + "\n"
                + "Attempt No: " + attemptNo + "\n"
                + "Recommendation: " + safe(recommendation) + "\n\n"
                + "- CRS System";
        sendAndLog(to, subject, body, "RECOVERY_PLAN");
    }

    public void sendMilestoneEmail(String to, String action, String studentId, String courseCode, int attemptNo,
                                   String title, String status, String grade, String remarks) throws SQLException {
        String subject = "CRS Milestone " + safe(action);
        String body = "Milestone update:\n\n"
                + "Student ID: " + safe(studentId) + "\n"
                + "Course Code: " + safe(courseCode) + "\n"
                + "Attempt No: " + attemptNo + "\n"
                + "Title: " + safe(title) + "\n"
                + "Status: " + safe(status) + "\n"
                + "Grade: " + safe(grade) + "\n"
                + "Remarks: " + safe(remarks) + "\n\n"
                + "- CRS System";
        sendAndLog(to, subject, body, "MILESTONE");
    }

    public void sendReportEmail(String to, String body) throws SQLException {
        sendAndLog(to, "CRS Academic Performance Report", body, "REPORT");
    }

    public List<Notification> listHistory(int limit) throws SQLException {
        return new NotificationDAO().listHistory(limit);
    }

    private void sendAndLog(String to, String subject, String body, String type) throws SQLException {
        NotificationDAO dao = new NotificationDAO();

        Notification n = new Notification();
        n.setRecipientEmail(to);
        n.setSubject(subject);
        n.setBody(body);
        n.setType(type);

        long id = dao.logQueued(n);

        try {
            EmailUtil.send(mailSession, to, subject, body);
            dao.markSent(id);
        } catch (Exception e) {
            dao.markFailed(id, e.getMessage());
        }
    }

    private String safe(String v) {
        return v == null ? "-" : v;
    }
}