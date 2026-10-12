package olle_christoffer.dto;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;


public final class OrderDTO {
    private final long id;
    private final long userId;
    private final String status;
    private final String statusLabel;
    private final boolean readyForPacking;
    private final BigDecimal total;
    private final OffsetDateTime createdAt;
    private final String customerName;
    private final List<OrderItemDTO> items;

    public OrderDTO(long id, long userId, String status, String statusLabel, boolean readyForPacking, BigDecimal total, OffsetDateTime createdAt, String customerName, List<OrderItemDTO> items) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.statusLabel = statusLabel;
        this.readyForPacking = readyForPacking;
        this.total = total;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.items = List.copyOf(items);
    }


    public long getId() { return id; }
    public long getUserId() { return userId; }
    public String getStatus() { return status; }
    public String getStatusLabel() { return statusLabel; }
    public boolean isReadyForPacking() { return readyForPacking; }
    public BigDecimal getTotal() { return total; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public String getCustomerName() { return customerName; }
    public List<OrderItemDTO> getItems() { return items; }
}