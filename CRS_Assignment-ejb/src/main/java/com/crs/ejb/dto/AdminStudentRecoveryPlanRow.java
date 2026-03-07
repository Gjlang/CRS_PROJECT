package com.crs.ejb.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AdminStudentRecoveryPlanRow implements Serializable {
    private long planId;
    private Long enrolmentId;
    private String courseCode;
    private String courseName;
    private int attemptNo;
    private String recommendation;
    private LocalDateTime createdAt;
    private long milestoneCount;

    public long getPlanId() {
        return planId;
    }
    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public Long getEnrolmentId() {
        return enrolmentId;
    }
    public void setEnrolmentId(Long enrolmentId) {
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

    public String getRecommendation() {
        return recommendation;
    }
    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getMilestoneCount() {
        return milestoneCount;
    }
    public void setMilestoneCount(long milestoneCount) {
        this.milestoneCount = milestoneCount;
    }
}