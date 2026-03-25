package com.crs.ejb.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportData implements Serializable {
    private String studentId;
    private String studentName;
    private String major;
    private int semester;
    private int year;
    private int yearOfStudy;
    private List<ReportCourseRow> rows = new ArrayList<>();
    private double cgpa;

    private String weakCoursesSummary;
    private String improvementRecommendation;
    private String suggestedRecoveryAction;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public List<ReportCourseRow> getRows() { return rows; }
    public void setRows(List<ReportCourseRow> rows) { this.rows = rows; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public String getWeakCoursesSummary() { return weakCoursesSummary; }
    public void setWeakCoursesSummary(String weakCoursesSummary) { this.weakCoursesSummary = weakCoursesSummary; }

    public String getImprovementRecommendation() { return improvementRecommendation; }
    public void setImprovementRecommendation(String improvementRecommendation) { this.improvementRecommendation = improvementRecommendation; }

    public String getSuggestedRecoveryAction() { return suggestedRecoveryAction; }
    public void setSuggestedRecoveryAction(String suggestedRecoveryAction) { this.suggestedRecoveryAction = suggestedRecoveryAction; }
}