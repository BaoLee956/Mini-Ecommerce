package org.example.miniecommerce.repository;

import org.example.miniecommerce.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {
    Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);

    List<OrderItem> findAll();

    List<OrderItem> findByOrderId(Long orderId);

    void save(OrderItem orderItem);

    void deleteByOrderIdAndProductId(Long orderId, Long productId);
}
