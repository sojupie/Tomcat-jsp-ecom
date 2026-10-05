package olle_christoffer.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import olle_christoffer.model.CustomerOrder;
import olle_christoffer.model.OrderItem;
import olle_christoffer.utilities.JDBC;

/** SQL operations for customer orders and their items. */
public class OrderDAO {

    public List<CustomerOrder> findAllForWarehouse() throws SQLException {
        String sql = "SELECT ord.id AS order_id, ord.user_id, ord.status, ord.total, ord.created_at, "
                + "usr.full_name AS customer_name, item.id AS item_id, item.product_id, "
                + "pro.name AS product_name, item.quantity, item.unit_price "
                + "FROM customer_order ord "
                + "JOIN app_user usr ON usr.id = ord.user_id "
                + "LEFT JOIN order_item item ON item.order_id = ord.id "
                + "LEFT JOIN product pro ON pro.id = item.product_id "
                + "ORDER BY ord.created_at DESC, ord.id DESC, item.id";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            Map<Long, CustomerOrder> ordersById = new LinkedHashMap<>();
            while (results.next()) {
                long orderId = results.getLong("order_id");
                CustomerOrder order = ordersById.get(orderId);
                if (order == null) {
                    order = new CustomerOrder();
                    order.setId(orderId);
                    order.setUserId(results.getLong("user_id"));
                    order.setStatus(CustomerOrder.Status.valueOf(results.getString("status")));
                    order.setTotal(results.getBigDecimal("total"));
                    order.setCreatedAt(results.getObject("created_at", OffsetDateTime.class));
                    order.setCustomerName(results.getString("customer_name"));
                    ordersById.put(orderId, order);
                }

                if (results.getObject("item_id") != null) {
                    OrderItem item = new OrderItem();
                    item.setId(results.getLong("item_id"));
                    item.setOrderId(orderId);
                    item.setProductId(results.getLong("product_id"));
                    item.setProductName(results.getString("product_name"));
                    item.setQuantity(results.getInt("quantity"));
                    item.setUnitPrice(results.getBigDecimal("unit_price"));
                    order.getItems().add(item);
                }
            }
            return new ArrayList<>(ordersById.values());
        }
    }

    public boolean markPacked(long orderId) throws SQLException {
        String sql = "UPDATE customer_order SET status = 'PACKED' "
                + "WHERE id = ? AND status IN ('PLACED', 'PACKING')";
        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            return statement.executeUpdate() == 1;
        }
    }

    public long insertOrder(Connection con, long userId, BigDecimal total) throws SQLException {
        String sql = "INSERT INTO customer_order (user_id, total) VALUES (?, ?)";

        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, userId);
            statement.setBigDecimal(2, total);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("The database did not return an order ID.");
                }
                return generatedKeys.getLong(1);
            }
        }
    }

    public void insertOrderItem(Connection con, long orderId, long productId,
                                int quantity, BigDecimal unitPrice) throws SQLException {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, unit_price) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.setLong(2, productId);
            statement.setInt(3, quantity);
            statement.setBigDecimal(4, unitPrice);
            statement.executeUpdate();
        }
    }
}
