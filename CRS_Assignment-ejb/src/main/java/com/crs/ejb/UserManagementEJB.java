package com.crs.ejb;

import com.crs.dao.UserDAO;
import com.crs.entity.User;
import com.crs.util.PasswordUtil;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.sql.SQLException;
import java.util.List;

@Stateless
public class UserManagementEJB {

    @EJB
    private NotificationEJB notificationEJB;

    public long createUser(User u, String plainPassword) throws SQLException {
        validateUser(u);

        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        u.setEmail(u.getEmail().trim().toLowerCase());
        u.setPasswordHash(PasswordUtil.hash(plainPassword));

        if (u.getRole() == null) {
            u.setRole("ACADEMIC_OFFICER");
        }

        if (!"COURSE_ADMIN".equals(u.getRole()) && !"ACADEMIC_OFFICER".equals(u.getRole())) {
            throw new IllegalArgumentException("Invalid role.");
        }

        u.setActive(true);

        UserDAO dao = new UserDAO();
        long id = dao.create(u);

        if (notificationEJB != null) {
            notificationEJB.sendUserAccountEmail(u.getEmail(), u.getFullName(), u.getRole());
        }

        return id;
    }

    public void updateUser(User u) throws SQLException {
        validateUser(u);

        if (u.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid userId.");
        }

        u.setEmail(u.getEmail().trim().toLowerCase());

        UserDAO dao = new UserDAO();
        dao.update(u);

        if (notificationEJB != null) {
            notificationEJB.sendUserUpdatedEmail(u.getEmail(), u.getFullName(), u.getRole(), u.isActive());
        }
    }

    public void deactivateUser(long userId) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId.");
        }

        UserDAO dao = new UserDAO();
        User existing = dao.findById(userId);

        dao.deactivate(userId);

        if (existing != null && notificationEJB != null && existing.getEmail() != null && !existing.getEmail().isBlank()) {
            notificationEJB.sendUserDeactivatedEmail(existing.getEmail(), existing.getFullName());
        }
    }

    public List<User> listUsers() throws SQLException {
        UserDAO dao = new UserDAO();
        List<User> list = dao.findAll();

        for (User u : list) {
            u.setPasswordHash(null);
        }

        return list;
    }

    public User findByEmail(String email) throws SQLException {
        if (email == null || email.isBlank()) {
            return null;
        }
        return new UserDAO().findByEmail(email.trim().toLowerCase());
    }

    public void resetPassword(long userId, String plainPassword) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId.");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        String hash = PasswordUtil.hash(plainPassword);
        new UserDAO().updatePasswordHash(userId, hash);
    }

    private void validateUser(User u) {
        if (u == null) throw new IllegalArgumentException("User is required.");
        if (u.getFullName() == null || u.getFullName().isBlank()) throw new IllegalArgumentException("Full name is required.");
        if (u.getEmail() == null || u.getEmail().isBlank()) throw new IllegalArgumentException("Email is required.");
        if (u.getRole() == null || u.getRole().isBlank()) throw new IllegalArgumentException("Role is required.");
    }
}