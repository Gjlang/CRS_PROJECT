package com.crs.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Enrolment implements Serializable {
    private long enrolmentId;
    private String studentId;
    private String courseCode;
    private int attemptNo;
    private String eligibilityStatus; // PASS/FAIL
    private String enrolmentStatus;   // PENDING/APPROVED/REJECTED
    private LocalDateTime createdAt;

    public Enrolment() {}

    public Enrolment(long enrolmentId, String studentId, String courseCode, int attemptNo, String eligibilityStatus, String enrolmentStatus, LocalDateTime createdAt) {
        this.enrolmentId = enrolmentId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.attemptNo = attemptNo;
        this.eligibilityStatus = eligibilityStatus;
        this.enrolmentStatus = enrolmentStatus;
        this.createdAt = createdAt;
    }

    public long getEnrolmentId() { return enrolmentId; }
    public void setEnrolmentId(long enrolmentId) { this.enrolmentId = enrolmentId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public int getAttemptNo() { return attemptNo; }
    public void setAttemptNo(int attemptNo) { this.attemptNo = attemptNo; }

    public String getEligibilityStatus() { return eligibilityStatus; }
    public void setEligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; }

    public String getEnrolmentStatus() { return enrolmentStatus; }
    public void setEnrolmentStatus(String enrolmentStatus) { this.enrolmentStatus = enrolmentStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override public String toString() {
        return "Enrolment{id=" + enrolmentId + ", studentId='" + studentId + "', courseCode='" + courseCode + "', attemptNo=" + attemptNo +
                ", eligibilityStatus='" + eligibilityStatus + "', enrolmentStatus='" + enrolmentStatus + "'}";
    }
}

