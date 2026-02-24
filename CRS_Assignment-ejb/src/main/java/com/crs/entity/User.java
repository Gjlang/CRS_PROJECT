package com.crs.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {
    private long userId;
    private String fullName;
    private String email;
    private String passwordHash; // stored salted hash
    private String role; // COURSE_ADMIN or ACADEMIC_OFFICER
    private boolean active;
    private LocalDateTime createdAt;

    public User() {}

    public User(long userId, String fullName, String email, String passwordHash, String role, boolean active, LocalDateTime createdAt) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override public String toString() {
        return "User{userId=" + userId + ", fullName='" + fullName + "', email='" + email + "', role='" + role + "', active=" + active + "}";
    }
}

