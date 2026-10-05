package olle_christoffer.model;
import java.math.BigDecimal;

/**
 * endast get/set-metoder.
 */

public class OrderItem {
    private long id;
    private long orderId;
    private long productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem() {}

    // get/set metoder
    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public long getOrderId() {return orderId;}

    public void setOrderId(long orderId) {this.orderId = orderId;}

    public long getProductId() {return productId;}

    public void setProductId(long productId) {this.productId = productId;}

    public String getProductName() {return productName;}

    public void setProductName(String productName) {this.productName = productName;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public BigDecimal getUnitPrice() {return unitPrice;}

    public void setUnitPrice(BigDecimal unitPrice) {this.unitPrice = unitPrice;}

    public BigDecimal getTotalSum() {return unitPrice.multiply(BigDecimal.valueOf(quantity));} // pris * kvantitet
}
