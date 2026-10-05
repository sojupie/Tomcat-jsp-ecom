package olle_christoffer.dao;

import olle_christoffer.model.Category;
import olle_christoffer.utilities.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT id, name, description FROM category ORDER BY name";
        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            List<Category> categories = new ArrayList<>();
            while (results.next()) {
                Category category = new Category();
                category.setId(results.getLong("id"));
                category.setName(results.getString("name"));
                category.setDescription(results.getString("description"));
                categories.add(category);
            }
            return categories;
        }
    }

}
