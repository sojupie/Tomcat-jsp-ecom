package olle_christoffer.mapper;
import olle_christoffer.dto.CartDTO;
import olle_christoffer.dto.CartItemDTO;
import olle_christoffer.dto.CategoryDTO;
import olle_christoffer.dto.OrderDTO;
import olle_christoffer.dto.OrderItemDTO;
import olle_christoffer.dto.ProductDTO;
import olle_christoffer.dto.UserDTO;
import olle_christoffer.model.Cart;
import olle_christoffer.model.CartItem;
import olle_christoffer.model.Category;
import olle_christoffer.model.CustomerOrder;
import olle_christoffer.model.OrderItem;
import olle_christoffer.model.Product;
import olle_christoffer.model.User;
import java.util.List;


public final class DtoMapper {
    private DtoMapper() {}


    public static ProductDTO toDto(Product p) {
        return new ProductDTO(p.getId(), p.getSku(), p.getName(), p.getDescription(), p.getPrice(), p.isActive(), p.getCategoryId(), p.getCategoryName(), p.getStock());
    }

    public static List<ProductDTO> toProductDtos(List<Product> products){
        return products.stream().map(DtoMapper::toDto).toList();
    }

    public static Product toModel(ProductDTO dto) {
        Product p = new Product();

        p.setId(dto.getId());
        p.setSku(dto.getSku());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setActive(dto.isActive());
        p.setCategoryId(dto.getCategoryId());
        p.setCategoryName(dto.getCategoryName());
        p.setStock(dto.getStock());

        return p;
    }

    public static CategoryDTO toDto(Category c) {
        return new CategoryDTO(c.getId(), c.getName(), c.getDescription());
    }

    public static List<CategoryDTO> toCategoryDtos(List<Category> categories){
        return categories.stream().map(DtoMapper::toDto).toList();
    }

    public static Category toModel(CategoryDTO dto){
        Category c = new Category();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        return c;
    }


    public static UserDTO toDto(User u) { //ej känslig info som lösenord
        return new UserDTO(u.getId(), u.getUsername(), u.getFullName(), u.getEmail(),
                u.getRole().name(), u.isActive());
    }

    public static List<UserDTO> toUserDtos(List<User> users) {
        return users.stream().map(DtoMapper::toDto).toList();
    }

    public static OrderItemDTO toDto(OrderItem item) {
        return new OrderItemDTO(item.getProductId(), item.getProductName(), item.getQuantity(), item.getUnitPrice());
    }

    public static OrderDTO toDto(CustomerOrder o) {
        List<OrderItemDTO> items = o.getItems().stream().map(DtoMapper::toDto).toList();

        return new OrderDTO(o.getId(), o.getUserId(), o.getStatus().name(), o.getStatusLabel(), o.isReadyForPacking(), o.getTotal(), o.getCreatedAt(), o.getCustomerName(), items);
    }

    public static List<OrderDTO> toOrderDtos(List<CustomerOrder> orders) {
        return orders.stream().map(DtoMapper::toDto).toList();
    }

    public static CartItemDTO toDto(CartItem item) {

        return new CartItemDTO(item.getProductId(), item.getProductName(), item.getQuantity(), item.getUnitPrice());
    }


    public static CartDTO toDto(Cart cart) {
        return new CartDTO(cart.getItems().stream().map(DtoMapper::toDto).toList());
    }

}