package com.crs.ejb.dto;

import java.io.Serializable;

public class ReportCourseRow implements Serializable {
    private String courseCode;
    private String courseTitle;
    private int creditHours;
    private String grade;
    private double gradePoint;

    public ReportCourseRow() {}

    public ReportCourseRow(String courseCode, String courseTitle, int creditHours, String grade, double gradePoint) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
        this.grade = grade;
        this.gradePoint = gradePoint;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public double getGradePoint() { return gradePoint; }
    public void setGradePoint(double gradePoint) { this.gradePoint = gradePoint; }
}

