package com.crs.ejb.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AdminStudentEnrolmentRow implements Serializable {
    private long enrolmentId;
    private String courseCode;
    private String courseName;
    private int attemptNo;
    private String eligibilityStatus;
    private String enrolmentStatus;
    private LocalDateTime createdAt;
    private String rejectReason;

    public long getEnrolmentId() {
        return enrolmentId;
    }
    public void setEnrolmentId(long enrolmentId) {
        this.enrolmentId = enrolmentId;
    }

    public String getCourseCode() {
        return courseCode;
    }
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getAttemptNo() {
        return attemptNo;
    }
    public void setAttemptNo(int attemptNo) {
        this.attemptNo = attemptNo;
    }

    public String getEligibilityStatus() {
        return eligibilityStatus;
    }
    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public String getEnrolmentStatus() {
        return enrolmentStatus;
    }
    public void setEnrolmentStatus(String enrolmentStatus) {
        this.enrolmentStatus = enrolmentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRejectReason() {
        return rejectReason;
    }
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}