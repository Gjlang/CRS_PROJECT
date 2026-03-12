package com.crs.ejb.dto;

import java.io.Serializable;

public class ReportCourseRow implements Serializable {
    private String courseCode;
    private String courseName;
    private int credits;
    private String grade;
    private double gradePoint;

    public ReportCourseRow() {
    }

    public ReportCourseRow(String courseCode, String courseName, int credits, String grade, double gradePoint) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.grade = grade;
        this.gradePoint = gradePoint;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }
}