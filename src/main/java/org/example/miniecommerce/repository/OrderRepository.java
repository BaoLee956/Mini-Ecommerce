package org.example.miniecommerce.repository;

import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
