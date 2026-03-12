package com.crs.ejb.dto;

import java.io.Serializable;

public class RecoveryCandidateRow implements Serializable {
    private String studentId;
    private String studentName;
    private String courseCode;
    private String courseName;
    private int attemptNo;
    private int failedComponentCount;

    public RecoveryCandidateRow() {}

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getAttemptNo() { return attemptNo; }
    public void setAttemptNo(int attemptNo) { this.attemptNo = attemptNo; }

    public int getFailedComponentCount() { return failedComponentCount; }
    public void setFailedComponentCount(int failedComponentCount) { this.failedComponentCount = failedComponentCount; }
}