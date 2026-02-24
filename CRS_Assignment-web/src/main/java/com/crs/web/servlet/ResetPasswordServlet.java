package com.crs.web.servlet;

import com.crs.ejb.UserManagementEJB;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/reset_password")
public class ResetPasswordServlet extends HttpServlet {

    @EJB
    private UserManagementEJB userManagementEJB;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        String newPassword = req.getParameter("new_password");

        HttpSession session = req.getSession(false);
        String expected = (session == null) ? null : (String) session.getAttribute("pwResetToken");
        Long userId = (session == null) ? null : (Long) session.getAttribute("pwResetUserId");

        if (expected == null || userId == null) {
            req.setAttribute("error", "No reset request found. Please use Forgot Password again.");
            req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
            return;
        }
        if (token == null || !token.equals(expected)) {
            req.setAttribute("error", "Invalid token.");
            req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
            return;
        }

        try {
            userManagementEJB.resetPassword(userId, newPassword);

            session.removeAttribute("pwResetToken");
            session.removeAttribute("pwResetUserId");

            req.setAttribute("message", "Password reset successful. Please login.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Reset failed: " + e.getMessage());
            req.getRequestDispatcher("/reset_password.jsp").forward(req, resp);
        }
    }
}

