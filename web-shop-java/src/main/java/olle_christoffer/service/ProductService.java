package olle_christoffer.service;

import olle_christoffer.dao.ProductDAO;
import olle_christoffer.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> findActiveProducts() throws SQLException {
        return productDAO.findAllActive();
    }

    public Optional<Product> findActiveProduct(long productId) throws SQLException {
        return productDAO.findById(productId).filter(Product::isActive);
    }

}
