package olle_christoffer.model;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * endast get/set-metoder.
 */

public class CustomerOrder {

    public enum Status {PLACED, PACKING, SHIPPED, CANCELLED}
    private long id;
    private long userId;
    private Status status = Status.PLACED; // ny beställning börjar som placerad
    private BigDecimal total;
    private OffsetDateTime createdAt;
    private List<OrderItem> items = new ArrayList<>();

    public CustomerOrder() {}

    // get/set metoder
    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public long getUserId() {return userId;}

    public void setUserId(long userId) {this.userId = userId;}

    public Status getStatus() {return status;}

    public void setStatus(Status status) {this.status = status;}

    public BigDecimal getTotal() {return total;}

    public void setTotal(BigDecimal total) {this.total = total;}

    public OffsetDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(OffsetDateTime createdAt) {this.createdAt = createdAt;}

    public List<OrderItem> getItems() {return items;}

    public void setItems(List<OrderItem> items) {this.items = items;}
}
