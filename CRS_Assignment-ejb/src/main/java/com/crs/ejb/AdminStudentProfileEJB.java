package com.crs.ejb;

import com.crs.dao.AdminStudentProfileDAO;
import com.crs.ejb.dto.AdminStudentListRow;
import com.crs.ejb.dto.AdminStudentProfileData;
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.util.List;

@Stateless
public class AdminStudentProfileEJB {

    private final AdminStudentProfileDAO dao = new AdminStudentProfileDAO();

    public List<AdminStudentListRow> listAllStudents() {
        try {
            return dao.listAllStudents();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load student list", e);
        }
    }

    public AdminStudentProfileData getStudentProfile(String studentId) {
        try {
            return dao.findStudentProfile(studentId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load student profile", e);
        }
    }
}