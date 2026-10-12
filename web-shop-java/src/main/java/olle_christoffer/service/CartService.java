package olle_christoffer.service;

import olle_christoffer.dto.CartDTO;
import olle_christoffer.dto.ProductDTO;
import olle_christoffer.mapper.DtoMapper;
import olle_christoffer.model.Cart;
import olle_christoffer.model.CartItem;

import java.sql.SQLException;
import java.util.ArrayList;

public class CartService {
    private final ProductService productService = new ProductService();

    public void addProduct(Cart cart, long productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        ProductDTO product = productService.findActiveProduct(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product is no longer available."));

        cart.addItem(product.getId(), product.getName(), product.getPrice(), quantity);
    }

    public boolean adjustToAvailableStock(Cart cart) throws SQLException {
        boolean adjusted = false;
        for (CartItem item : new ArrayList<>(cart.getItems())) {
            ProductDTO product = productService.findActiveProduct(item.getProductId()).orElse(null);
            if (product == null || product.getStock() <= 0) {
                cart.removeItem(item.getProductId());
                adjusted = true;
            } else if (item.getQuantity() > product.getStock()) {
                cart.updateQuantity(item.getProductId(), product.getStock());
                adjusted = true;
            }
        }
        return adjusted;
    }

    public void removeProduct(Cart cart, long productId) {
        cart.removeItem(productId);
    }


    public CartDTO toView(Cart cart) {
        return DtoMapper.toDto(cart);
    }

}