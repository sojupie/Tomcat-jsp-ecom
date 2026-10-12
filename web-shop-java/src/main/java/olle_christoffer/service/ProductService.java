package olle_christoffer.service;

import olle_christoffer.dao.ProductDAO;
import olle_christoffer.dto.ProductDTO;
import olle_christoffer.mapper.DtoMapper;
import olle_christoffer.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }


    public List<ProductDTO> findActiveProducts() throws SQLException {
        return DtoMapper.toProductDtos(productDAO.findAllActive());
    }

    public Optional<ProductDTO> findActiveProduct(long productId) throws SQLException {
        return productDAO.findById(productId).filter(Product::isActive).map(DtoMapper::toDto);
    }

}