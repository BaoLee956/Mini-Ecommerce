package org.example.miniecommerce.service;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLookupServiceImpl implements OrderLookupService {

    private final OrderRepository repo;


    @Override
    public Order findByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
