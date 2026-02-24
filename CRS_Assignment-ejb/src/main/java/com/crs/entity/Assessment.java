package com.crs.entity;

import java.io.Serializable;

public class Assessment implements Serializable {
    private long assessmentId;
    private String courseCode;
    private String componentName;
    private double weightPercent;

    public Assessment() {}

    public Assessment(long assessmentId, String courseCode, String componentName, double weightPercent) {
        this.assessmentId = assessmentId;
        this.courseCode = courseCode;
        this.componentName = componentName;
        this.weightPercent = weightPercent;
    }

    public long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(long assessmentId) { this.assessmentId = assessmentId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public double getWeightPercent() { return weightPercent; }
    public void setWeightPercent(double weightPercent) { this.weightPercent = weightPercent; }

    @Override public String toString() {
        return "Assessment{id=" + assessmentId + ", courseCode='" + courseCode + "', component='" + componentName + "', weight=" + weightPercent + "}";
    }
}

