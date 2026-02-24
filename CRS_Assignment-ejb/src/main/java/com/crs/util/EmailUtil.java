package com.crs.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public final class EmailUtil {
    private EmailUtil() {}

    public static void send(Session mailSession, String to, String subject, String body) throws MessagingException {
        if (mailSession == null) {
            // Allow assignments to run without SMTP configured.
            // Caller should treat this as "sent" only if you want; we throw to mark FAILED by default.
            throw new MessagingException("Mail Session not configured (java:comp/env/mail/crsMailSession).");
        }

        MimeMessage msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress("noreply@crs.local"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        Transport.send(msg);
    }
}

