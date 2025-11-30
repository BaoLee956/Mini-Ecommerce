package org.example.miniecommerce.service;

import org.example.miniecommerce.entity.Order;

public interface OrderLookupService {
    Order findByIdOrThrow(Long id);
}
