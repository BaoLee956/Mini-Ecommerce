package org.example.miniecommerce.repository;

import org.example.miniecommerce.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {
    Optional<OrderItem> findById(Long id);

    List<OrderItem> findAll();

    List<OrderItem> findByOrderId(Long orderId);

    void save(OrderItem orderItem);

    void deleteById(Long id);
}
