package com.crs.ejb.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportData implements Serializable {
    private String studentId;
    private String studentName;
    private String major;        
    private int semester;
    private int year;
    private int studentYear;    
    private List<ReportCourseRow> rows = new ArrayList<>();
    private double cgpa;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getMajor() { return major; }           
    public void setMajor(String major) { this.major = major; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getStudentYear() { return studentYear; } 
    public void setStudentYear(int studentYear) { this.studentYear = studentYear; }

    public List<ReportCourseRow> getRows() { return rows; }
    public void setRows(List<ReportCourseRow> rows) { this.rows = rows; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
}