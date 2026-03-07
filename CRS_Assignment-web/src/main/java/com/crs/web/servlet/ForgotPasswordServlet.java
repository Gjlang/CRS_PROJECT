package com.crs.web.servlet;
import com.crs.ejb.NotificationEJB;
import com.crs.ejb.UserManagementEJB;
import com.crs.entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@WebServlet("/forgot_password")
public class ForgotPasswordServlet extends HttpServlet {
    @EJB
    private UserManagementEJB userManagementEJB;
    @EJB
    private NotificationEJB notificationEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/forgot_password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            req.setAttribute("error", "Email is required.");
            req.getRequestDispatcher("/forgot_password.jsp").forward(req, resp);
            return;
        }
        String normalized = email.trim().toLowerCase();
        try {
            User u = userManagementEJB.findByEmail(normalized);
            String genericMsg = "If the email exists, a reset token has been sent.";
            if (u == null) {
                req.setAttribute("message", genericMsg);
                req.getRequestDispatcher("/forgot_password.jsp").forward(req, resp);
                return;
            }
            byte[] buf = new byte[24];
            new SecureRandom().nextBytes(buf);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
            HttpSession session = req.getSession(true);
            session.setAttribute("pwResetToken", token);
            session.setAttribute("pwResetUserId", u.getUserId());
            notificationEJB.sendPasswordResetEmail(u.getEmail(), token);
            req.setAttribute("message", genericMsg + " (Check notifications table if SMTP not set.)");
            req.getRequestDispatcher("/forgot_password.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Failed: " + e.getMessage());
            req.getRequestDispatcher("/forgot_password.jsp").forward(req, resp);
        }
    }
}