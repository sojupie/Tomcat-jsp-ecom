package olle_christoffer.dto;

import java.math.BigDecimal;
import java.util.List;

public final class CartDTO {

    private final List<CartItemDTO> items;
    private final int totalQuantity;
    private final BigDecimal total;

    public CartDTO(List<CartItemDTO> items){
        this.items = List.copyOf(items);
        this.totalQuantity = items.stream().mapToInt(CartItemDTO::getQuantity).sum();
        this.total = items.stream().map(CartItemDTO::getTotalSum).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public List<CartItemDTO> getItems() {
        return items;
    }

    public int getTotalQuantity(){
        return totalQuantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

}
