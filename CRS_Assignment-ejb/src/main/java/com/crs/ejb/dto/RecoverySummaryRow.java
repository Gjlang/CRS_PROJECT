package com.crs.ejb.dto;

public class RecoverySummaryRow {
    private final String studentId;
    private final String courseCode;
    private final int attemptCount;
    private final String eligibilityStatus;
    private final String enrolmentStatus;
    private final String rejectReason;
    private final String recoveryPlanSummary;
    private final int milestoneCount;

    public RecoverySummaryRow(
            String studentId,
            String courseCode,
            int attemptCount,
            String eligibilityStatus,
            String enrolmentStatus,
            String rejectReason,
            String recoveryPlanSummary,
            int milestoneCount
    ) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.attemptCount = attemptCount;
        this.eligibilityStatus = eligibilityStatus;
        this.enrolmentStatus = enrolmentStatus;
        this.rejectReason = rejectReason;
        this.recoveryPlanSummary = recoveryPlanSummary;
        this.milestoneCount = milestoneCount;
    }

    // getters for JSP EL
    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public int getAttemptCount() { return attemptCount; }
    public String getEligibilityStatus() { return eligibilityStatus; }
    public String getEnrolmentStatus() { return enrolmentStatus; }
    public String getRejectReason() { return rejectReason; }
    public String getRecoveryPlanSummary() { return recoveryPlanSummary; }
    public int getMilestoneCount() { return milestoneCount; }
}