package com.crs.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class Milestone implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long milestoneId;
    private Long planId;

    private int studyWeek;
    private String task;

    private String title;     // ✅ required by DAO/EJB
    private LocalDate dueDate;

    private String status;    // PENDING / DONE
    private String grade;
    private String remarks;   // ✅ required by DAO/EJB

    public Long getMilestoneId() {
        return milestoneId;
    }
    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public Long getPlanId() {
        return planId;
    }
    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public int getStudyWeek() {
        return studyWeek;
    }
    public void setStudyWeek(int studyWeek) {
        this.studyWeek = studyWeek;
    }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }

    // ✅ missing before
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    // ✅ missing before
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}