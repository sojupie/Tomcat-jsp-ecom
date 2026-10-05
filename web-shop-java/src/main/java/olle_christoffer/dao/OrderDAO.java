package olle_christoffer.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** SQL operations for customer orders and their items. */
public class OrderDAO {

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
