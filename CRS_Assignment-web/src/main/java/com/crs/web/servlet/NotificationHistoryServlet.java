package com.crs.web.servlet;
import com.crs.ejb.NotificationEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/notification_history")
public class NotificationHistoryServlet extends HttpServlet {
    @EJB
    private NotificationEJB notificationEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("history", notificationEJB.listHistory(50));
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load history: " + e.getMessage());
        }
        req.setAttribute("activePage", "admin_notifications"); 
        req.getRequestDispatcher("/admin/notification_history.jsp").forward(req, resp);
    }
}