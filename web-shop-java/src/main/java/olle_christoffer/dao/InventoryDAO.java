package olle_christoffer.dao;
import olle_christoffer.utilities.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQL metoder för lagersaldot
 */


public class InventoryDAO {

    public boolean decreaseStock(Connection con, long productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity has to > 0");
        }

        String sql = "UPDATE inventory SET quantity = quantity - ?, updated_at = now() "
                + "WHERE product_id = ? AND quantity >= ?";

        try (PreparedStatement prepS = con.prepareStatement(sql)) {
            prepS.setInt(1, quantity);
            prepS.setLong(2, productId);
            prepS.setInt(3, quantity);


            return prepS.executeUpdate() == 1;
        }
    }

    public boolean increaseStock(Connection con, long productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity has to be more than 0");
        }

        String sql = "UPDATE inventory SET quantity = quantity + ?, updated_at = now() WHERE product_id = ?";


        try (PreparedStatement prepS = con.prepareStatement(sql)) {
            prepS.setInt(1, quantity);
            prepS.setLong(2, productId);

            return prepS.executeUpdate() == 1;
        }
    }

    public void setQuantity(long productId, int quantity) throws SQLException {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity can not be negative");
        }


        try (Connection con = JDBC.getConnection()) {
            setQuantity(con, productId, quantity);
        }
    }

    public void setQuantity(Connection con, long productId, int quantity) throws SQLException {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity cannot be negative");
        }

        String sql = "INSERT INTO inventory (product_id, quantity) VALUES (?, ?) "
                + "ON CONFLICT (product_id) DO UPDATE SET quantity = EXCLUDED.quantity, updated_at = now()";
        try (PreparedStatement prepS = con.prepareStatement(sql)) {

            prepS.setLong(1, productId);
            prepS.setInt(2, quantity);
            prepS.executeUpdate();
        }
    }
}
