package com.crs.entity;

import java.io.Serializable;

public class Course implements Serializable {
    private String courseCode;
    private String courseTitle;
    private int creditHours;

    public Course() {}

    public Course(String courseCode, String courseTitle, int creditHours) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.creditHours = creditHours;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }

    @Override public String toString() {
        return "Course{courseCode='" + courseCode + "', courseTitle='" + courseTitle + "', creditHours=" + creditHours + "}";
    }
}

