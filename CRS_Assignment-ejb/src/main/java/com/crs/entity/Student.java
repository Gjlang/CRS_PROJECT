package com.crs.entity;

import java.io.Serializable;

public class Student implements Serializable {
    private String studentId;
    private String firstName;
    private String lastName;
    private String major;
    private int year;
    private String email;
    private boolean active;

    public Student() {}

    public Student(String studentId, String firstName, String lastName, String major, int year, String email, boolean active) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
        this.year = year;
        this.email = email;
        this.active = active;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() {
        return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
    }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Student{studentId='" + studentId + "', firstName='" + firstName + "', lastName='" + lastName +
               "', major='" + major + "', year=" + year + ", email='" + email + "', active=" + active + "}";
    }
}