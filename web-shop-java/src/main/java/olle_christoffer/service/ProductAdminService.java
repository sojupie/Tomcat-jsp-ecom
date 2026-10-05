package olle_christoffer.service;

import olle_christoffer.dao.CategoryDAO;
import olle_christoffer.dao.InventoryDAO;
import olle_christoffer.dao.ProductDAO;
import olle_christoffer.model.Product;
import olle_christoffer.utilities.JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductAdminService {
    private final AuthorizationService authorizationService = new AuthorizationService();
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    public List<Product> listProducts(long adminId) throws SQLException {
        authorizationService.requireAdmin(adminId);
        return productDAO.findAll();
    }

    public Product findProduct(long adminId, long productId) throws SQLException {
        authorizationService.requireAdmin(adminId);
        return productDAO.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product does not exist."));
    }

    public long saveProduct(long adminId, Product product) throws SQLException {
        authorizationService.requireAdmin(adminId);
        validate(product);

        try (Connection connection = JDBC.getConnection()) {
            connection.setAutoCommit(false);
            try {
                if (product.getCategoryId() > 0
                        && categoryDAO.findById(connection, product.getCategoryId()).isEmpty()) {
                    throw new IllegalArgumentException("The selected category does not exist.");
                }
                if (productDAO.existsBySku(connection, product.getSku(), product.getId())) {
                    throw new IllegalArgumentException("A product with this SKU already exists.");
                }

                long productId;
                if (product.getId() == 0) {
                    productId = productDAO.insert(connection, product);
                } else {
                    if (!productDAO.update(connection, product)) {
                        throw new IllegalArgumentException("Product does not exist.");
                    }
                    productId = product.getId();
                }
                inventoryDAO.setQuantity(connection, productId, product.getStock());
                connection.commit();
                return productId;
            } catch (SQLException | RuntimeException exception) {
                rollback(connection, exception);
                throw exception;
            }
        }
    }

    private void validate(Product product) {
        if (product == null || product.getId() < 0) {
            throw new IllegalArgumentException("Invalid product.");
        }
        String sku = product.getSku() == null ? "" : product.getSku().trim();
        String name = product.getName() == null ? "" : product.getName().trim();
        String description = product.getDescription() == null ? null : product.getDescription().trim();
        if (sku.isEmpty() || sku.length() > 64) {
            throw new IllegalArgumentException("SKU is required and must be at most 64 characters.");
        }
        if (name.isEmpty() || name.length() > 200) {
            throw new IllegalArgumentException("Product name is required and must be at most 200 characters.");
        }
        if (product.getPrice() == null || product.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Price must be zero or greater.");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        if (product.getCategoryId() < 0) {
            throw new IllegalArgumentException("Invalid category.");
        }
        product.setSku(sku);
        product.setName(name);
        product.setDescription(description == null || description.isBlank() ? null : description);
    }

    private void rollback(Connection connection, Exception original) {
        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            original.addSuppressed(rollbackException);
        }
    }
}
