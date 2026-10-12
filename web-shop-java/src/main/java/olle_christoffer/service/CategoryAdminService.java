package olle_christoffer.service;
import olle_christoffer.dao.CategoryDAO;
import olle_christoffer.dto.CategoryDTO;
import olle_christoffer.mapper.DtoMapper;
import olle_christoffer.model.Category;
import olle_christoffer.utilities.JDBC;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class CategoryAdminService {
    private final AuthorizationService authorizationService = new AuthorizationService();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<CategoryDTO> listCategories(long adminId) throws SQLException {
        authorizationService.requireAdmin(adminId);
        return DtoMapper.toCategoryDtos(categoryDAO.findAll());
    }

    public CategoryDTO findCategory(long adminId, long categoryId) throws SQLException {
        authorizationService.requireAdmin(adminId);
        return categoryDAO.findById(categoryId).map(DtoMapper::toDto).orElseThrow(() ->
                new IllegalArgumentException("category nonexistent"));
    }

    public long saveCategory(long adminId, CategoryDTO form) throws SQLException {
        authorizationService.requireAdmin(adminId);
        if (form == null) {
            throw new IllegalArgumentException("unvalid cat");
        }

        Category category = DtoMapper.toModel(form);
        validate(category);

        try (Connection connection = JDBC.getConnection()) {
            connection.setAutoCommit(false);
            try {
                if (categoryDAO.existsByName(connection, category.getName(), category.getId())) {
                    throw new IllegalArgumentException("similar category with that name already in");
                }

                long categoryId;
                if (category.getId() == 0) {
                    categoryId = categoryDAO.insert(connection, category);
                } else {
                    if (!categoryDAO.update(connection, category)) {
                        throw new IllegalArgumentException("cat nonexistent");
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
            throw new IllegalArgumentException("invalid category");
        }
        String name = category.getName() == null ? "" : category.getName().trim();
        if (name.isEmpty() || name.length() > 120) {
            throw new IllegalArgumentException("category name is required and must be at most 120 characters");
        }
        String description = category.getDescription() == null ? null : category.getDescription().trim();
        category.setName(name);
        category.setDescription(description == null || description.isBlank() ? null : description);
    }
}
