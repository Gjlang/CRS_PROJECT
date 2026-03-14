package com.crs.web.servlet;

import com.crs.dao.UserDAO;
import com.crs.ejb.UserManagementEJB;
import com.crs.entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/reset_password")
public class ResetPasswordServlet extends HttpServlet {

    @EJB
    private UserManagementEJB userManagementEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        String newPassword = req.getParameter("new_password");

        if (token == null || token.isBlank()) {
            req.setAttribute("error", "Reset token is required.");
            req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
            return;
        }

        if (newPassword == null || newPassword.length() < 6) {
            req.setAttribute("error", "New password must be at least 6 characters.");
            req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
            return;
        }

        try {
            UserDAO dao = new UserDAO();
            User u = dao.findByResetToken(token.trim());

            if (u == null) {
                req.setAttribute("error", "Invalid or expired token.");
                req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
                return;
            }

            userManagementEJB.resetPassword(u.getUserId(), newPassword);
            dao.clearResetToken(u.getUserId());

            req.setAttribute("message", "Password reset successful. Please login.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", "Reset failed: " + e.getMessage());
            req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
        }
    }
}