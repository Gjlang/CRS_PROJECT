package com.crs.ejb.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EligibilityResult implements Serializable {
    private String studentId;
    private double cgpa;
    private int failedCourseCount;
    private boolean eligible;
    private List<String> reasons = new ArrayList<>();

    public EligibilityResult() {}

    public EligibilityResult(String studentId, double cgpa, int failedCourseCount, boolean eligible, List<String> reasons) {
        this.studentId = studentId;
        this.cgpa = cgpa;
        this.failedCourseCount = failedCourseCount;
        this.eligible = eligible;
        if (reasons != null) this.reasons = reasons;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public int getFailedCourseCount() { return failedCourseCount; }
    public void setFailedCourseCount(int failedCourseCount) { this.failedCourseCount = failedCourseCount; }

    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }

    @Override public String toString() {
        return "EligibilityResult{studentId='" + studentId + "', cgpa=" + cgpa + ", failedCourseCount=" + failedCourseCount + ", eligible=" + eligible + "}";
    }
}

