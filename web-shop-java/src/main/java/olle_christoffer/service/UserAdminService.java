package olle_christoffer.service;

import olle_christoffer.dao.UserDAO;
import olle_christoffer.model.User;
import olle_christoffer.utilities.JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * hantering av användare,
 * service ser om anroparen är en aktiv admin i databasen
 * SecurityException för obehörig anrop
 */

public class UserAdminService {
    private final UserDAO userDAO = new UserDAO();

    public List<User> listUsers(long actingAdminId) throws SQLException {
        requireActiveAdmin(userDAO.findById(actingAdminId));


        List<User> users = userDAO.findAll();
        for (User user : users) {
            user.setPasswordHash(null);

        }

        return users;
    }

    public void changeRoleAndActive(long actingAdminId, long targetUserId, User.Role newRole, boolean newActive)
            throws SQLException {
        if (newRole == null) {
            throw new IllegalArgumentException("Välj en roll.");
        }

        if (actingAdminId == targetUserId) {
            throw new IllegalArgumentException("Du kan inte ändra din egen roll eller spärra dig själv.");
        }


        try (Connection con = JDBC.getConnection()) {
            con.setAutoCommit(false);
            try {
                Optional<User> actor;
                Optional<User> target;
                if (actingAdminId < targetUserId) {
                    actor = userDAO.findByIdForUpdate(con, actingAdminId);
                    target = userDAO.findByIdForUpdate(con, targetUserId);

                } else {
                    target = userDAO.findByIdForUpdate(con, targetUserId);
                    actor = userDAO.findByIdForUpdate(con, actingAdminId);
                }

                requireActiveAdmin(actor); // kontroll
                if (target.isEmpty()) {
                    throw new IllegalArgumentException("Användaren finns inte.");
                }

                userDAO.updateRoleAndActive(con, targetUserId, newRole, newActive);
                con.commit();

            } catch (SQLException | RuntimeException exception) {
                con.rollback();
                throw exception;
            }
        }
    }

    private void requireActiveAdmin(Optional<User> user) {
        if (user.isEmpty() || !user.get().isActive() || !user.get().isAdmin()) {
            throw new SecurityException("Endast administratörer får hantera användare.");
        }
    }
}

