package olle_christoffer.service;

import olle_christoffer.dao.OrderDAO;
import olle_christoffer.dto.OrderDTO;
import olle_christoffer.mapper.DtoMapper;
import java.sql.SQLException;
import java.util.List;

public class WarehouseOrderService {
    private final AuthorizationService authorizationService = new AuthorizationService();
    private final OrderDAO orderDAO = new OrderDAO();

    public List<OrderDTO> listOrders(long warehouseUserId) throws SQLException {
        authorizationService.requireWarehouse(warehouseUserId);
        return DtoMapper.toOrderDtos(orderDAO.findAllForWarehouse());
    }

    public void markPacked(long warehouseUserId, long orderId) throws SQLException {
        authorizationService.requireWarehouse(warehouseUserId);
        if (orderId <= 0) {
            throw new IllegalArgumentException("Invalid order ID.");
        }
        if (!orderDAO.markPacked(orderId)) {
            throw new IllegalStateException("The order does not exist or is no longer ready to be packed.");
        }
    }
}
