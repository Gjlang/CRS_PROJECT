package com.crs.entity;

import java.io.Serializable;

public class Course implements Serializable {
    private String courseId;
    private String courseName;
    private int credits;
    private String semester;
    private String instructor;
    private Integer capacity;

    public Course() {}

    public Course(String courseId, String courseName, int credits, String semester, String instructor, Integer capacity) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
        this.instructor = instructor;
        this.capacity = capacity;
    }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    @Override
    public String toString() {
        return "Course{courseId='" + courseId + "', courseName='" + courseName + "', credits=" + credits +
               ", semester='" + semester + "', instructor='" + instructor + "', capacity=" + capacity + "}";
    }
}