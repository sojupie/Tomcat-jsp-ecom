package olle_christoffer.service;

import olle_christoffer.dao.CategoryDAO;
import olle_christoffer.model.Category;
import olle_christoffer.utilities.JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryAdminService {
    private final AuthorizationService authorizationService = new AuthorizationService();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> listCategories(long adminId) throws SQLException {
        authorizationService.requireAdmin(adminId);
        return categoryDAO.findAll();
    }

    public Category findCategory(long adminId, long categoryId) throws SQLException {
        authorizationService.requireAdmin(adminId);
        return categoryDAO.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category does not exist."));
    }

    public long saveCategory(long adminId, Category category) throws SQLException {
        authorizationService.requireAdmin(adminId);
        validate(category);

        try (Connection connection = JDBC.getConnection()) {
            connection.setAutoCommit(false);
            try {
                if (categoryDAO.existsByName(connection, category.getName(), category.getId())) {
                    throw new IllegalArgumentException("A category with this name already exists.");
                }

                long categoryId;
                if (category.getId() == 0) {
                    categoryId = categoryDAO.insert(connection, category);
                } else {
                    if (!categoryDAO.update(connection, category)) {
                        throw new IllegalArgumentException("Category does not exist.");
                    }
                    categoryId = category.getId();
                }
                connection.commit();
                return categoryId;
            } catch (SQLException | RuntimeException exception) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
                throw exception;
            }
        }
    }

    private void validate(Category category) {
        if (category == null || category.getId() < 0) {
            throw new IllegalArgumentException("Invalid category.");
        }
        String name = category.getName() == null ? "" : category.getName().trim();
        if (name.isEmpty() || name.length() > 120) {
            throw new IllegalArgumentException("Category name is required and must be at most 120 characters.");
        }
        String description = category.getDescription() == null ? null : category.getDescription().trim();
        category.setName(name);
        category.setDescription(description == null || description.isBlank() ? null : description);
    }
}
