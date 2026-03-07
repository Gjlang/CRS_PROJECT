package com.crs.ejb.dto;

import java.io.Serializable;

public class AdminStudentResultRow implements Serializable {
    private long resultId;
    private String courseCode;
    private String courseName;
    private String assessmentComponent;
    private String grade;
    private double gradePoint;
    private boolean failed;
    private int attemptNo;
    private int semester;
    private int year;
    private int yearOfStudy;

    public long getResultId() {
        return resultId;
    }
    public void setResultId(long resultId) {
        this.resultId = resultId;
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

    public String getAssessmentComponent() {
        return assessmentComponent;
    }
    public void setAssessmentComponent(String assessmentComponent) {
        this.assessmentComponent = assessmentComponent;
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

    public boolean isFailed() {
        return failed;
    }
    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public int getAttemptNo() {
        return attemptNo;
    }
    public void setAttemptNo(int attemptNo) {
        this.attemptNo = attemptNo;
    }

    public int getSemester() {
        return semester;
    }
    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
}