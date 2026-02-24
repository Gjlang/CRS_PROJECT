package com.crs.web.servlet;

import com.crs.ejb.UserManagementEJB;
import com.crs.entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    @EJB
    private UserManagementEJB userManagementEJB;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> users = userManagementEJB.listUsers();
            req.setAttribute("users", users);
        } catch (Exception e) {
            req.setAttribute("error", "Failed to load users: " + e.getMessage());
        }
        req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("create".equals(action)) {
                User u = new User();
                u.setFullName(req.getParameter("full_name"));
                u.setEmail(req.getParameter("email"));
                u.setRole(req.getParameter("role"));
                u.setActive(true);
                userManagementEJB.createUser(u, req.getParameter("password"));
                req.setAttribute("message", "User created.");
            } else if ("update".equals(action)) {
                User u = new User();
                u.setUserId(Long.parseLong(req.getParameter("user_id")));
                u.setFullName(req.getParameter("full_name"));
                u.setEmail(req.getParameter("email"));
                u.setRole(req.getParameter("role"));
                u.setActive("1".equals(req.getParameter("active")));
                userManagementEJB.updateUser(u);
                req.setAttribute("message", "User updated.");
            } else if ("deactivate".equals(action)) {
                long id = Long.parseLong(req.getParameter("user_id"));
                userManagementEJB.deactivateUser(id);
                req.setAttribute("message", "User deactivated.");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Action failed: " + e.getMessage());
        }
        doGet(req, resp);
    }
}

