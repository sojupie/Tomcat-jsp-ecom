package olle_christoffer.dao;
import olle_christoffer.model.User;
import olle_christoffer.utilities.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQL metoder för operationer gällande användare i databasen
 */


public class UserDAO {

    private static final String SELECT_USER =
            "SELECT usr.id, usr.username, usr.password_hash, usr.full_name, usr.email, "
                    + "usr.role, usr.active, usr.created_at "
                    + "FROM app_user usr ";


    public Optional<User> findByUsername(String username) throws SQLException { // används vid inloggning
        String sql = SELECT_USER + "WHERE usr.username = ?";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql)) {

            prepS.setString(1, username); // för förebygga SQL-injection
            try (ResultSet res = prepS.executeQuery()) {
                return res.next() ? Optional.of(createObject(res)) : Optional.empty();
            }
        }
    }

    public Optional<User> findById(long id) throws SQLException {
        String sql = SELECT_USER + "WHERE usr.id = ?";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql)) {

            prepS.setLong(1, id); // -||-
            try (ResultSet res = prepS.executeQuery()) {
                return res.next() ? Optional.of(createObject(res)) : Optional.empty();
            }
        }
    }

    public List<User> findAll() throws SQLException { // användare sorterade efter användarnamn
        String sql = SELECT_USER + "ORDER BY usr.username";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql);
             ResultSet res = prepS.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (res.next()) {
                users.add(createObject(res));
            }

            return users;
        }
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO app_user (username, password_hash, full_name, email, role, active) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql, new String[] {"id"})) {

            prepS.setString(1, user.getUsername());
            prepS.setString(2, user.getPasswordHash());
            prepS.setString(3, user.getFullName());
            prepS.setString(4, user.getEmail());
            prepS.setString(5, user.getRole().name());
            prepS.setBoolean(6, user.isActive());
            prepS.executeUpdate();

            try (ResultSet keys = prepS.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }
        }
    }

    public boolean updateRoleAndActive(long id, User.Role role, boolean active) throws SQLException { // för admin
        String sql = "UPDATE app_user SET role = ?, active = ? WHERE id = ?";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql)) {

            prepS.setString(1, role.name());
            prepS.setBoolean(2, active);
            prepS.setLong(3, id);

            return prepS.executeUpdate() == 1;
        }
    }


    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM app_user WHERE username = ?";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql)) {

            prepS.setString(1, username);
            try (ResultSet res = prepS.executeQuery()) {
                return res.next();
            }
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM app_user WHERE email = ?";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql)) {

            prepS.setString(1, email);
            try (ResultSet res = prepS.executeQuery()) {
                return res.next();
            }
        }
    }

    private User createObject(ResultSet res) throws SQLException {
        User user = new User();

        user.setId(res.getLong("id"));
        user.setUsername(res.getString("username"));
        user.setPasswordHash(res.getString("password_hash"));
        user.setFullName(res.getString("full_name"));
        user.setEmail(res.getString("email"));
        user.setRole(User.Role.valueOf(res.getString("role")));
        user.setActive(res.getBoolean("active"));
        user.setCreatedAt(res.getObject("created_at", OffsetDateTime.class));

        return user;
    }
}