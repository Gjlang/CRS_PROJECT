package com.crs.ejb;

import com.crs.dao.NotificationDAO;
import com.crs.entity.Notification;
import com.crs.util.EmailUtil;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.Session;
import java.sql.SQLException;

@Stateless
public class NotificationEJB {

    // Payara JavaMail Session JNDI: mail/crsMailSession
    @Resource(lookup = "mail/crsMailSession")
    private Session mailSession;

    public void sendUserAccountEmail(String to, String name, String role) throws SQLException {
        String subject = "CRS Account Created";
        String body = "Hello " + name + ",\n\nYour CRS account has been created with role: " + role + ".\n\n- CRS System";
        sendAndLog(to, subject, body, "USER_MGMT");
    }

    public void sendPasswordResetEmail(String to, String token) throws SQLException {
        String subject = "CRS Password Reset";
        String body = "Use this reset token to reset your password: " + token
                + "\n\nIf you did not request this, ignore this email.";
        sendAndLog(to, subject, body, "PASSWORD_RESET");
    }

    public void sendMilestoneReminderEmail(String to, String subject, String body) throws SQLException {
        sendAndLog(to, subject, body, "MILESTONE");
    }

    public void sendReportEmail(String to, String body) throws SQLException {
        sendAndLog(to, "CRS Academic Performance Report", body, "REPORT");
    }

    public java.util.List<com.crs.entity.Notification> listHistory(int limit) throws java.sql.SQLException {
        return new com.crs.dao.NotificationDAO().listHistory(limit);
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
}