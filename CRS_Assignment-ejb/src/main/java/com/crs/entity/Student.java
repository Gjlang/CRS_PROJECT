package com.crs.entity;

import java.io.Serializable;

public class Student implements Serializable {
    private String studentId;
    private String studentName;
    private String program;
    private int yearOfStudy;
    private boolean active;

    public Student() {}

    public Student(String studentId, String studentName, String program, int yearOfStudy, boolean active) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.program = program;
        this.yearOfStudy = yearOfStudy;
        this.active = active;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override public String toString() {
        return "Student{studentId='" + studentId + "', studentName='" + studentName + "', program='" + program + "', yearOfStudy=" + yearOfStudy + ", active=" + active + "}";
    }
}

