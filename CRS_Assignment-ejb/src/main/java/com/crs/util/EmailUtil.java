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
            throw new MessagingException("Mail Session not configured (mail/crsMailSession).");
        }

        String fromAddress = mailSession.getProperty("mail.from");
        if (fromAddress == null || fromAddress.isBlank()) {
            fromAddress = mailSession.getProperty("mail.user");
        }
        if (fromAddress == null || fromAddress.isBlank()) {
            throw new MessagingException("Mail Session configured but missing mail.from / mail.user.");
        }

        MimeMessage msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(fromAddress));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");

        Transport.send(msg);
    }
}