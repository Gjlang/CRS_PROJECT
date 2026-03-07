package com.crs.ejb.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminStudentProfileData implements Serializable {
    private String studentId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String major;
    private int year;
    private String email;
    private boolean active;

    private List<AdminStudentEnrolmentRow> enrolments = new ArrayList<>();
    private List<AdminStudentResultRow> results = new ArrayList<>();
    private List<AdminStudentRecoveryPlanRow> recoveryPlans = new ArrayList<>();
    private List<AdminStudentMilestoneRow> milestones = new ArrayList<>();

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        refreshFullName();
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        refreshFullName();
    }

    public String getFullName() {
        return fullName;
    }

    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public List<AdminStudentEnrolmentRow> getEnrolments() {
        return enrolments;
    }
    public void setEnrolments(List<AdminStudentEnrolmentRow> enrolments) {
        this.enrolments = enrolments;
    }

    public List<AdminStudentResultRow> getResults() {
        return results;
    }
    public void setResults(List<AdminStudentResultRow> results) {
        this.results = results;
    }

    public List<AdminStudentRecoveryPlanRow> getRecoveryPlans() {
        return recoveryPlans;
    }
    public void setRecoveryPlans(List<AdminStudentRecoveryPlanRow> recoveryPlans) {
        this.recoveryPlans = recoveryPlans;
    }

    public List<AdminStudentMilestoneRow> getMilestones() {
        return milestones;
    }
    public void setMilestones(List<AdminStudentMilestoneRow> milestones) {
        this.milestones = milestones;
    }

    private void refreshFullName() {
        String fn = firstName == null ? "" : firstName.trim();
        String ln = lastName == null ? "" : lastName.trim();
        this.fullName = (fn + " " + ln).trim();
    }
}