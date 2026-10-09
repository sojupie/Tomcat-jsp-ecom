package olle_christoffer.dto;

import java.math.BigDecimal;

public final class CartItemDTO {
    private final long productId;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

    public CartItemDTO(long productId, String productName, int quantity, BigDecimal unitPrice){
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public long getProductId(){
        return productId;
    }

    public String getProductName(){
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalSum(){
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

}