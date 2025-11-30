package org.example.miniecommerce.service;

import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderLookupServiceImpl implements OrderLookupService {

    private final OrderRepository repo;

    public OrderLookupServiceImpl(OrderRepository repo) {
        this.repo = repo;
    }

    @Override
    public Order findByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
