package olle_christoffer.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Varukorg för självaste sessionen i Http,
 * vid köp omvandlas det den innehåller till CustomerOrder samt OrderItems
 */

public class Cart implements Serializable {

    private final List<CartItem> items = new ArrayList<>();
    public List<CartItem> getItems() {return items;}

    public void addItem(long productId, String productName, BigDecimal unitPrice, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("n have to be more than 0");
        }

        CartItem existing = findByProductId(productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity); // om varan redan är i korg ökas endast antalet
        } else {
            items.add(new CartItem(productId, productName, unitPrice, quantity));
        }
    }

    public void removeItem(long productId) {
        items.removeIf(i -> i.getProductId() == productId);
    }

    public void updateQuantity(long productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
            return;
        }

        CartItem existing = findByProductId(productId);
        if (existing != null) {
            existing.setQuantity(quantity);
        }
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getTotal() {
        return items.stream().map(CartItem::getTotalSum).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CartItem findByProductId(long productId) {
        for (CartItem item : items) {
            if (item.getProductId() == productId) {
                return item;
            }
        }

        return null;
    }
}