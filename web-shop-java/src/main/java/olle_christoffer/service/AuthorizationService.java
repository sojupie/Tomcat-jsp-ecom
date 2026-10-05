package olle_christoffer.service;

import olle_christoffer.dao.UserDAO;
import olle_christoffer.model.User;

import java.sql.SQLException;
import java.util.Optional;

/** Checks the current database role before a protected operation is performed. */
public class AuthorizationService {
    private final UserDAO userDAO = new UserDAO();

    public void requireAdmin(long userId) throws SQLException {
        requireRole(userId, User.Role.ADMIN);
    }

    public void requireWarehouse(long userId) throws SQLException {
        requireRole(userId, User.Role.WAREHOUSE);
    }

    private void requireRole(long userId, User.Role requiredRole) throws SQLException {
        Optional<User> user = userDAO.findById(userId);
        if (user.isEmpty() || !user.get().isActive() || user.get().getRole() != requiredRole) {
            throw new SecurityException("You are not allowed to perform this action.");
        }
    }
}
