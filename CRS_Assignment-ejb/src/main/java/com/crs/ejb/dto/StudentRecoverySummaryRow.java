package com.crs.ejb.dto;

import java.io.Serializable;

public class StudentRecoverySummaryRow implements Serializable {
    private String studentId;
    private String studentName;
    private int failedCoursesCount;

    public StudentRecoverySummaryRow() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getFailedCoursesCount() {
        return failedCoursesCount;
    }

    public void setFailedCoursesCount(int failedCoursesCount) {
        this.failedCoursesCount = failedCoursesCount;
    }
}