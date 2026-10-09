package olle_christoffer.dto;
import java.math.BigDecimal;


public final class ProductDTO {
    private final long id;
    private final String sku;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean active;
    private final long categoryId;
    private final String categoryName;
    private final int stock;

    public ProductDTO(long id, String sku, String name, String description, BigDecimal price, boolean active, long categoryId, String categoryName, int stock) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.stock = stock;
    }


    public long getId() {return id;}
    public String getSku() { return sku;}
    public String getName() { return name;}
    public String getDescription() { return description;}
    public BigDecimal getPrice() {return price;}
    public boolean isActive() {return active; }
    public long getCategoryId() {return categoryId;}
    public String getCategoryName() {return categoryName;}
    public int getStock() { return stock;}
    public boolean isInStock() {
        return stock > 0;
    }

}