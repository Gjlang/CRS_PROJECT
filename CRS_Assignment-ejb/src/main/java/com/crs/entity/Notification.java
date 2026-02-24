package com.crs.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Serializable {
    private long notificationId;
    private String recipientEmail;
    private String subject;
    private String body;
    private String type;
    private String status; // QUEUED/SENT/FAILED
    private LocalDateTime createdAt;
    private String errorMessage;

    public Notification() {}

    public Notification(long notificationId, String recipientEmail, String subject, String body, String type,
                        String status, LocalDateTime createdAt, String errorMessage) {
        this.notificationId = notificationId;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.errorMessage = errorMessage;
    }

    public long getNotificationId() { return notificationId; }
    public void setNotificationId(long notificationId) { this.notificationId = notificationId; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    @Override public String toString() {
        return "Notification{id=" + notificationId + ", to='" + recipientEmail + "', type='" + type + "', status='" + status + "'}";
    }
}

