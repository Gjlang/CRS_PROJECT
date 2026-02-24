package com.crs.entity;

import java.io.Serializable;
import java.time.LocalDate;

public class Milestone implements Serializable {
    private long milestoneId;
    private long planId;
    private int studyWeek;
    private String task;
    private LocalDate dueDate;
    private String status; // TODO/DONE
    private String grade;  // optional grading entry

    public Milestone() {}

    public Milestone(long milestoneId, long planId, int studyWeek, String task, LocalDate dueDate, String status, String grade) {
        this.milestoneId = milestoneId;
        this.planId = planId;
        this.studyWeek = studyWeek;
        this.task = task;
        this.dueDate = dueDate;
        this.status = status;
        this.grade = grade;
    }

    public long getMilestoneId() { return milestoneId; }
    public void setMilestoneId(long milestoneId) { this.milestoneId = milestoneId; }

    public long getPlanId() { return planId; }
    public void setPlanId(long planId) { this.planId = planId; }

    public int getStudyWeek() { return studyWeek; }
    public void setStudyWeek(int studyWeek) { this.studyWeek = studyWeek; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override public String toString() {
        return "Milestone{id=" + milestoneId + ", planId=" + planId + ", week=" + studyWeek + ", status='" + status + "'}";
    }
}

