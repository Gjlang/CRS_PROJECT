package com.crs.ejb.dto;

import java.io.Serializable;

public class RecoverySummaryRow implements Serializable {
    private String studentId;
    private String courseCode;
    private int attemptCount;
    private String eligibilityStatus;
    private String enrolmentStatus;
    private String rejectReason;
    private String recoveryPlanSummary;
    private int milestoneCount;

    public RecoverySummaryRow() {
    }

    public RecoverySummaryRow(String studentId, String courseCode, int attemptCount,
                              String eligibilityStatus, String enrolmentStatus,
                              String rejectReason, String recoveryPlanSummary,
                              int milestoneCount) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.attemptCount = attemptCount;
        this.eligibilityStatus = eligibilityStatus;
        this.enrolmentStatus = enrolmentStatus;
        this.rejectReason = rejectReason;
        this.recoveryPlanSummary = recoveryPlanSummary;
        this.milestoneCount = milestoneCount;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
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

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getRecoveryPlanSummary() {
        return recoveryPlanSummary;
    }

    public void setRecoveryPlanSummary(String recoveryPlanSummary) {
        this.recoveryPlanSummary = recoveryPlanSummary;
    }

    public int getMilestoneCount() {
        return milestoneCount;
    }

    public void setMilestoneCount(int milestoneCount) {
        this.milestoneCount = milestoneCount;
    }
}