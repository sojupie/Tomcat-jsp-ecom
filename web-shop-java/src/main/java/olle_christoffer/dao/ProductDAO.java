package olle_christoffer.dao;
import olle_christoffer.model.Product;
import olle_christoffer.utilities.JDBC;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
        try (Connection con = JDBC.getConnection()) {
            return findById(con, id);
        }
    }

    public Optional<Product> findById(Connection con, long id) throws SQLException {
        String sql = SELECT_PRODUCT + "WHERE pro.id = ?";

        try (PreparedStatement prepS = con.prepareStatement(sql)) {
            prepS.setLong(1, id);
            try (ResultSet res = prepS.executeQuery()) {
                return res.next() ? Optional.of(createObject(res)) : Optional.empty();
            }
        }
    }

    public long insert(Connection con, Product product) throws SQLException {
        String sql = "INSERT INTO product (category_id, sku, name, description, price, active) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setProductParameters(statement, product);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("The database did not return a product ID.");
                }
                return generatedKeys.getLong(1);
            }
        }
    }

    public boolean update(Connection con, Product product) throws SQLException {
        String sql = "UPDATE product SET category_id = ?, sku = ?, name = ?, description = ?, "
                + "price = ?, active = ? WHERE id = ?";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            setProductParameters(statement, product);
            statement.setLong(7, product.getId());
            return statement.executeUpdate() == 1;
        }
    }

    public boolean existsBySku(Connection con, String sku, long exceptProductId) throws SQLException {
        String sql = "SELECT 1 FROM product WHERE sku = ? AND id <> ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, sku);
            statement.setLong(2, exceptProductId);
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
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

    private void setProductParameters(PreparedStatement statement, Product product) throws SQLException {
        if (product.getCategoryId() > 0) {
            statement.setLong(1, product.getCategoryId());
        } else {
            statement.setNull(1, Types.BIGINT);
        }
        statement.setString(2, product.getSku());
        statement.setString(3, product.getName());
        statement.setString(4, product.getDescription());
        statement.setBigDecimal(5, product.getPrice());
        statement.setBoolean(6, product.isActive());
    }
}
