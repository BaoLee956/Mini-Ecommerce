package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.shipping.CreateShipmentRequest;
import org.example.miniecommerce.dto.shipping.UpdateShipmentRequest;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.Shipping;
import org.example.miniecommerce.factory.ShippingFactory;
import org.example.miniecommerce.repository.ShippingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShippingService {

    private final ShippingRepository repo;
    private final OrderLookupService orderLookup;

    public ShippingService(ShippingRepository repo, OrderLookupService orderLookup) {
        this.repo = repo;
        this.orderLookup = orderLookup;
    }

    @Transactional
    public Shipping create(CreateShipmentRequest req) {
        Order order = orderLookup.findByIdOrThrow(req.orderId());
        Shipping s = ShippingFactory.fromCreateRequest(req, order);

        repo.save(s);

        // Sau khi save, query lại để lấy Shipping mới nhất
        return repo.findByOrderId(order.getId())
                   .stream()
                   .reduce((first, second) -> second)
                   .orElse(s);
    }

    @Transactional
    public Shipping update(UpdateShipmentRequest req) {
        Shipping s = repo.findById(req.shippingId())
                         .orElseThrow(() -> new IllegalArgumentException("Shipping not found"));

        ShippingFactory.applyUpdate(s, req);

        repo.update(s);

        return s;
    }
}