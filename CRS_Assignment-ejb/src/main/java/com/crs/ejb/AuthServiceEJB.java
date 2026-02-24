package com.crs.ejb;

import com.crs.dao.UserDAO;
import com.crs.entity.User;
import com.crs.util.PasswordUtil;
import jakarta.ejb.Stateless;
import java.sql.SQLException;

@Stateless
public class AuthServiceEJB {

    public User login(String email, String plainPassword) throws SQLException {
        if (email == null || email.isBlank() || plainPassword == null) return null;

        UserDAO userDAO = new UserDAO();
        User u = userDAO.findByEmail(email.trim().toLowerCase());
        if (u == null) return null;
        if (!u.isActive()) return null;
        if (!PasswordUtil.verify(plainPassword, u.getPasswordHash())) return null;

        // Do NOT return password hash to presentation tier (defense-in-depth)
        u.setPasswordHash(null);
        return u;
    }
}

