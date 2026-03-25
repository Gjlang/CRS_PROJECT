package com.crs.entity;

import java.io.Serializable;

public class StudentResult implements Serializable {
    private long resultId;
    private String studentId;
    private String courseCode;
    private long assessmentId;
    private int attemptNo;
    private String grade;
    private double gradePoint;
    private boolean failed;
    private int semester;
    private int year;
    private int yearOfStudy;

    // added for full table support
    private Long enrolmentId;

    public StudentResult() {}

    public StudentResult(long resultId, String studentId, String courseCode, long assessmentId, int attemptNo,
                         String grade, double gradePoint, boolean failed, int semester, int year, int yearOfStudy) {
        this.resultId = resultId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.assessmentId = assessmentId;
        this.attemptNo = attemptNo;
        this.grade = grade;
        this.gradePoint = gradePoint;
        this.failed = failed;
        this.semester = semester;
        this.year = year;
        this.yearOfStudy = yearOfStudy;
    }

    public long getResultId() { return resultId; }
    public void setResultId(long resultId) { this.resultId = resultId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public long getAssessmentId() { return assessmentId; }
    public void setAssessmentId(long assessmentId) { this.assessmentId = assessmentId; }

    public int getAttemptNo() { return attemptNo; }
    public void setAttemptNo(int attemptNo) { this.attemptNo = attemptNo; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public double getGradePoint() { return gradePoint; }
    public void setGradePoint(double gradePoint) { this.gradePoint = gradePoint; }

    public boolean isFailed() { return failed; }
    public void setFailed(boolean failed) { this.failed = failed; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public Long getEnrolmentId() { return enrolmentId; }
    public void setEnrolmentId(Long enrolmentId) { this.enrolmentId = enrolmentId; }

    @Override
    public String toString() {
        return "StudentResult{resultId=" + resultId +
                ", studentId='" + studentId + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", assessmentId=" + assessmentId +
                ", attemptNo=" + attemptNo +
                ", grade='" + grade + '\'' +
                ", gradePoint=" + gradePoint +
                ", failed=" + failed +
                ", semester=" + semester +
                ", year=" + year +
                ", yearOfStudy=" + yearOfStudy +
                ", enrolmentId=" + enrolmentId +
                '}';
    }
}