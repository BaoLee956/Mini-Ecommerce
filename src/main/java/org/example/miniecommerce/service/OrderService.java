package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.order.*;

import java.util.List;


public interface OrderService {
    // POST /api/orders
    OrderResponse createOrder(Long userId, CreateOrderRequest request);
    // GET /api/orders/me
    List<OrderResponse> getMyOrders(Long userId);
    // GET /api/orders/{id}
    OrderResponse getOrderById(Long id, Long userId);
    // PUT /api/orders/{id}/status
    OrderStatusResponse updateStatus(Long id, UpdateOrderStatusRequest request, Long userId);
    // DELETE /api/orders/{id}
    DeleteResponse deleteOrder(Long id, Long userId);


}
