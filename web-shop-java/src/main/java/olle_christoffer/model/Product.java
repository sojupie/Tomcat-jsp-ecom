package olle_christoffer.model;
import java.math.BigDecimal;

/**
 * Modellklass för objekt av produkt,
 * med variabel för möjliga attributer,
 * och endast get/setmetoder för respektive,
 * samt par metoder för se om active samt i lager.
 */

public class Product {
    private long id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private boolean active;
    private long categoryId;
    private String categoryName;
    private int stockQ;

    public Product() {}

    // get/set metoder
    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSku() {return sku;}

    public void setSku(String sku) {this.sku = sku;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public BigDecimal getPrice() {return price;}

    public void setPrice(BigDecimal price) {this.price = price;}

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}

    public long getCategoryId() {return categoryId;}

    public void setCategoryId(long categoryId) {this.categoryId = categoryId;}

    public String getCategoryName() {return categoryName;}

    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}

    public int getStockQ() {return stockQ;}

    public void setStockQ(int stockQ) {this.stockQ = stockQ;}

    public boolean isInStock() {return stockQ > 0;}
}