package olle_christoffer.dao;
import olle_christoffer.model.Product;
import olle_christoffer.utilities.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQL-kod-metoder för operationer på produkter i databasen,
 */


public class ProductDAO {

    private static final String SELECT_PRODUCT =
            "SELECT pro.id, pro.sku, pro.name, pro.description, pro.price, pro.active, "
                    + "cat.id AS category_id, cat.name AS category_name, "
                    + "COALESCE(i.quantity, 0) AS stock_quantity "
                    + "FROM product pro "
                    + "LEFT JOIN category cat ON cat.id = pro.category_id "
                    + "LEFT JOIN inventory i ON i.product_id = pro.id "; // om produkt saknar lagerrad visas den med 0 i lager


    public List<Product> findAll() throws SQLException {
        String sql = SELECT_PRODUCT + "ORDER BY pro.name";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql);
             ResultSet res = prepS.executeQuery()) {

            List<Product> products = new ArrayList<>();
            while (res.next()) {
                products.add(createObject(res));
            }

            return products;
        }
    }

    public List<Product> findAllActive() throws SQLException { // aktiva produkter sorterade efter namnet
        String sql = SELECT_PRODUCT + "WHERE pro.active = true ORDER BY pro.name";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql);
             ResultSet res = prepS.executeQuery()) {

            List<Product> products = new ArrayList<>();
            while (res.next()) {
                products.add(createObject(res));
            }

            return products;
        }
    }

    public Optional<Product> findById(long id) throws SQLException {
        String sql = SELECT_PRODUCT + "WHERE pro.id = ?";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql)) {

            prepS.setLong(1, id); // för förebygga SQL-injection
            try (ResultSet res = prepS.executeQuery()) {
                return res.next() ? Optional.of(createObject(res)) : Optional.empty();
            }
        }
    }

    public List<Product> findByCategory(long categoryId) throws SQLException { // samtliga aktiva produkter i given kategori
        String sql = SELECT_PRODUCT + "WHERE pro.active = true AND cat.id = ? ORDER BY pro.name";

        try (Connection con = JDBC.getConnection();
             PreparedStatement prepS = con.prepareStatement(sql)) {

            prepS.setLong(1, categoryId); // -||-
            try (ResultSet res = prepS.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (res.next()) {
                    products.add(createObject(res));
                }

                return products;
            }
        }
    }

    private Product createObject(ResultSet res) throws SQLException { //omvandlar ett resultat till modell-objektet Product
        Product product = new Product();

        product.setId(res.getLong("id"));
        product.setSku(res.getString("sku"));
        product.setName(res.getString("name"));
        product.setDescription(res.getString("description"));
        product.setPrice(res.getBigDecimal("price"));
        product.setActive(res.getBoolean("active"));
        product.setCategoryId(res.getLong("category_id"));
        product.setCategoryName(res.getString("category_name"));
        product.setStock(res.getInt("stock_quantity"));

        return product;
    }
}
