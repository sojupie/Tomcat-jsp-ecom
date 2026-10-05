package olle_christoffer.service;

import olle_christoffer.dao.UserDAO;
import olle_christoffer.model.AuthenticatedUser;
import olle_christoffer.model.User;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class AccountService {
    private final UserDAO userDAO = new UserDAO();
    private final PasswordCodec passwordCodec = new PasswordCodec();

    public AuthenticatedUser register(String username, String fullName, String email, char[] password)
            throws SQLException {
        try {
            username = username == null ? "" : username.trim();
            fullName = fullName == null ? "" : fullName.trim();
            email = email == null ? "" : email.trim();
            validateRegistration(username, fullName, email, password);

            if (userDAO.existsByUsername(username) || userDAO.existsByEmail(email)) {
                throw new IllegalArgumentException("Username or email is already in use.");
            }

            User user = new User();
            user.setUsername(username);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setRole(User.Role.CUSTOMER);
            user.setActive(true);
            user.setPasswordHash(passwordCodec.hash(password));
            userDAO.insert(user);
            return new AuthenticatedUser(user);
        } finally {
            clear(password);
        }
    }

    public AuthenticatedUser login(String username, char[] password) throws SQLException {
        try {
            if (username == null || username.isBlank() || password == null || password.length == 0) {
                throw new IllegalArgumentException("Enter a username and password.");
            }

            Optional<User> found = userDAO.findByUsername(username.trim());
            if (found.isEmpty() || !found.get().isActive()
                    || !passwordCodec.verify(password, found.get().getPasswordHash())) {
                throw new IllegalArgumentException("Invalid username or password.");
            }
            return new AuthenticatedUser(found.get());
        } finally {
            clear(password);
        }
    }

    private void validateRegistration(String username, String fullName, String email, char[] password) {
        if (username.isBlank() || username.length() > 80) {
            throw new IllegalArgumentException("Username is required and must be at most 80 characters.");
        }
        if (fullName.isBlank() || fullName.length() > 160) {
            throw new IllegalArgumentException("Full name is required and must be at most 160 characters.");
        }
        if (email.isBlank() || email.length() > 254
                || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        if (password == null || password.length < 8 || password.length > 128) {
            throw new IllegalArgumentException("Password must be between 8 and 128 characters.");
        }
    }

    private void clear(char[] password) {
        if (password != null) {
            Arrays.fill(password, '\0');
        }
    }
}
