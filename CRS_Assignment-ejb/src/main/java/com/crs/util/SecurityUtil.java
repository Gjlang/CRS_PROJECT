package com.crs.util;
import jakarta.servlet.http.*;

public class SecurityUtil {

    public static boolean requireLogin(HttpServletRequest req, HttpServletResponse resp) throws java.io.IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return false;
        }
        if (session.getAttribute("role") == null) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return false;
        }
        return true;
    }

    public static boolean requireRole(HttpServletRequest req, HttpServletResponse resp, String requiredRole)
            throws java.io.IOException {

        if (!requireLogin(req, resp)) return false;

        String role = (String) req.getSession(false).getAttribute("role");
        if (!requiredRole.equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}