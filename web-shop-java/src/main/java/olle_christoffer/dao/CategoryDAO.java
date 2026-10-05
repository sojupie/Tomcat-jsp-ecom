package olle_christoffer.dao;

import olle_christoffer.model.Category;
import olle_christoffer.utilities.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAO {
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT id, name, description FROM category ORDER BY name";
        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            List<Category> categories = new ArrayList<>();
            while (results.next()) {
                categories.add(createCategory(results));
            }
            return categories;
        }
    }

    public Optional<Category> findById(long id) throws SQLException {
        try (Connection connection = JDBC.getConnection()) {
            return findById(connection, id);
        }
    }

    public Optional<Category> findById(Connection connection, long id) throws SQLException {
        String sql = "SELECT id, name, description FROM category WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet results = statement.executeQuery()) {
                return results.next() ? Optional.of(createCategory(results)) : Optional.empty();
            }
        }
    }

    public long insert(Connection connection, Category category) throws SQLException {
        String sql = "INSERT INTO category (name, description) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("The database did not return a category ID.");
                }
                return generatedKeys.getLong(1);
            }
        }
    }

    public boolean update(Connection connection, Category category) throws SQLException {
        String sql = "UPDATE category SET name = ?, description = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setLong(3, category.getId());
            return statement.executeUpdate() == 1;
        }
    }

    public boolean existsByName(Connection connection, String name, long exceptCategoryId) throws SQLException {
        String sql = "SELECT 1 FROM category WHERE name = ? AND id <> ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setLong(2, exceptCategoryId);
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        }
    }

    private Category createCategory(ResultSet results) throws SQLException {
        Category category = new Category();
        category.setId(results.getLong("id"));
        category.setName(results.getString("name"));
        category.setDescription(results.getString("description"));
        return category;
    }
}
