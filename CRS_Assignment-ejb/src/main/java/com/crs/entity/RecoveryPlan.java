package com.crs.entity;
import java.io.Serializable;
import java.time.LocalDateTime;
public class RecoveryPlan implements Serializable {
    private long planId;
    private String studentId;
    private String courseCode;
    private int attemptNo;
    private String recommendation;
    private long createdByUserId;
    private LocalDateTime createdAt;

    // 2.1 — new field
    private Long enrolmentId;

    public RecoveryPlan() {}
    public RecoveryPlan(long planId, String studentId, String courseCode, int attemptNo, String recommendation, long createdByUserId, LocalDateTime createdAt) {
        this.planId = planId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.attemptNo = attemptNo;
        this.recommendation = recommendation;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
    }
    public long getPlanId() { return planId; }
    public void setPlanId(long planId) { this.planId = planId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public int getAttemptNo() { return attemptNo; }
    public void setAttemptNo(int attemptNo) { this.attemptNo = attemptNo; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(long createdByUserId) { this.createdByUserId = createdByUserId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // 2.1 — getter/setter for enrolmentId
    public Long getEnrolmentId() { return enrolmentId; }
    public void setEnrolmentId(Long enrolmentId) { this.enrolmentId = enrolmentId; }

    @Override public String toString() {
        return "RecoveryPlan{planId=" + planId + ", studentId='" + studentId + "', courseCode='" + courseCode + "', attemptNo=" + attemptNo + ", enrolmentId=" + enrolmentId + "}";
    }
}