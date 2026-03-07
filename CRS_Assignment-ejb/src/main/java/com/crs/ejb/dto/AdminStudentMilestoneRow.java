package com.crs.ejb.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class AdminStudentMilestoneRow implements Serializable {
    private long milestoneId;
    private long planId;
    private String courseCode;
    private String courseName;
    private int studyWeek;
    private String title;
    private String task;
    private LocalDate dueDate;
    private String status;
    private String grade;
    private String remarks;

    public long getMilestoneId() {
        return milestoneId;
    }
    public void setMilestoneId(long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public long getPlanId() {
        return planId;
    }
    public void setPlanId(long planId) {
        this.planId = planId;
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

    public int getStudyWeek() {
        return studyWeek;
    }
    public void setStudyWeek(int studyWeek) {
        this.studyWeek = studyWeek;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}