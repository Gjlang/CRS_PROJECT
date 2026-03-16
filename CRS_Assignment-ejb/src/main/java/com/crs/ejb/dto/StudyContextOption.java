package com.crs.ejb.dto;

import java.io.Serializable;

public class StudyContextOption implements Serializable {
    private static final long serialVersionUID = 1L;

    private int semester;
    private int year;
    private int yearOfStudy;

    public StudyContextOption() {
    }

    public StudyContextOption(int semester, int year, int yearOfStudy) {
        this.semester = semester;
        this.year = year;
        this.yearOfStudy = yearOfStudy;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
}