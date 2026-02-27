package com.crs.ejb;

import com.crs.dao.RecoverySummaryReportDAO;
import com.crs.ejb.dto.RecoverySummaryRow;

import jakarta.ejb.Stateless;
import java.sql.SQLException;
import java.util.List;

@Stateless
public class RecoverySummaryReportEJB {

    private final RecoverySummaryReportDAO dao = new RecoverySummaryReportDAO();

    public List<RecoverySummaryRow> getRecoverySummary() {
        try {
            return dao.fetchRecoverySummary();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch recovery summary report", e);
        }
    }
}