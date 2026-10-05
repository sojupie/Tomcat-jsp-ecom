package olle_christoffer.service;

import olle_christoffer.dao.InventoryDAO;
import olle_christoffer.dao.OrderDAO;
import olle_christoffer.dao.ProductDAO;
import olle_christoffer.model.Cart;
import olle_christoffer.model.CartItem;
import olle_christoffer.model.Product;
import olle_christoffer.utilities.JDBC;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderService {

    private final ProductDAO productDAO = new ProductDAO();
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    /**
     * Sparar varukorg som en order och reducerar stock kvantitet i samma transaktion
     * Kallare bör enbart rensa session cart efter att denna metod returnerat
     */
    public long placeOrder(long userId, Cart cart) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("A valid user is required to place an order.");
        }
        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("The cart is empty.");
        }

        List<RequestedLine> requestedLines = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            if (item == null || item.getProductId() <= 0 || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("The cart contains an invalid item.");
            }
            requestedLines.add(new RequestedLine(item.getProductId(), item.getQuantity()));
        }
        requestedLines.sort(Comparator.comparingLong(RequestedLine::productId));

        try (Connection con = JDBC.getConnection()) {
            con.setAutoCommit(false);
            try {
                List<OrderLine> orderLines = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;

                for (RequestedLine requested : requestedLines) {
                    Product product = productDAO.findById(con, requested.productId())
                            .filter(Product::isActive)
                            .orElseThrow(() -> new IllegalStateException(
                                    "A product in the cart is no longer available."));

                    if (product.getStock() < requested.quantity()
                            || !inventoryDAO.decreaseStock(con, requested.productId(), requested.quantity())) {
                        throw new IllegalStateException(
                                "There is not enough stock to complete the order.");
                    }

                    BigDecimal unitPrice = product.getPrice();
                    orderLines.add(new OrderLine(requested.productId(), requested.quantity(), unitPrice));
                    total = total.add(unitPrice.multiply(BigDecimal.valueOf(requested.quantity())));
                }

                long orderId = orderDAO.insertOrder(con, userId, total);
                for (OrderLine line : orderLines) {
                    orderDAO.insertOrderItem(con, orderId, line.productId(), line.quantity(), line.unitPrice());
                }

                con.commit();
                return orderId;
            } catch (SQLException | RuntimeException exception) {
                try {
                    con.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
                throw exception;
            }
        }
    }

    private record RequestedLine(long productId, int quantity) { }

    private record OrderLine(long productId, int quantity, BigDecimal unitPrice) { }
}
