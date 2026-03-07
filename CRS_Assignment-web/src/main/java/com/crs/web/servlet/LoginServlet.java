package com.crs.web.servlet;
import com.crs.ejb.AuthServiceEJB;
import com.crs.entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @EJB
    private AuthServiceEJB authServiceEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(">>> HIT LoginServlet.doGet /login");
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            User u = authServiceEJB.login(email, password);
            if (u == null) {
                req.setAttribute("error", "Invalid credentials or inactive account.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("userId", u.getUserId());
            session.setAttribute("role", u.getRole());
            session.setAttribute("name", u.getFullName());

            if ("COURSE_ADMIN".equals(u.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else if ("ACADEMIC_OFFICER".equals(u.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/academic/dashboard");
            } else {
                session.invalidate();
                req.setAttribute("error", "Unknown role.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            req.setAttribute("error", "Login failed: " + e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}