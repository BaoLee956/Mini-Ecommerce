package org.example.miniecommerce.service.order;

import org.example.miniecommerce.dto.order.*;
import org.example.miniecommerce.entity.OrderStatus;
import org.example.miniecommerce.service.order.decorator.OrderDecoratorName;

import java.math.BigDecimal;
import java.util.List;


public interface OrderService {
    // POST /api/orders
    OrderResponse createOrder(Long userId, CreateOrderRequest request);

    // GET /api/orders/me
    List<OrderResponse> getMyOrders(Long userId);

    // GET /api/orders/{id}
    OrderResponse getOrderById(Long id, Long userId);

    OrderResponse getOrderById(Long id);

    // PUT /api/orders/{id}/status
    OrderStatusResponse updateStatus(Long id, UpdateOrderStatusRequest request, Long userId);

    void updateStatus(Long id, OrderStatus status);

    // DELETE /api/orders/{id}
    DeleteResponse deleteOrder(Long id, Long userId);

    void addFee(Long orderId, OrderDecoratorName name, BigDecimal fee);


}
