package olle_christoffer.model;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * rep. en rad i varukorgen i HttpSession,
 * som blir OrderItem om kund genomför köp,
 * endast get/set metoder nedan
 */

public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;

    public CartItem() {}

    public CartItem(long productId, String productName, BigDecimal unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalSum() { // pris * kvantitet
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}