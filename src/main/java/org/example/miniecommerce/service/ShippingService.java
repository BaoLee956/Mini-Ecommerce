package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.shipping.CreateShipmentRequest;
import org.example.miniecommerce.dto.shipping.UpdateShipmentRequest;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.Shipping;
import org.example.miniecommerce.repository.ShippingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ShippingService {

    private final ShippingRepository repo;
    private final OrderLookupService orderLookup;
    private final JdbcTemplate jdbc;

    public ShippingService(ShippingRepository repo, OrderLookupService orderLookup, JdbcTemplate jdbc) {
        this.repo = repo;
        this.orderLookup = orderLookup;
        this.jdbc = jdbc;
    }

    @Transactional
    public Shipping create(CreateShipmentRequest req) {
        Order order = orderLookup.findByIdOrThrow(req.orderId());

        Shipping s = new Shipping();
        s.setOrder(order);
        s.setDeliveryAddress(req.deliveryAddress());
        s.setCity(req.city());
        s.setPostalCode(req.postalCode());
        s.setCountry(req.country());
        s.setCarrierName(req.carrierName());
        s.setTrackingNumber(req.trackingNumber());
        s.setShippingCost(req.shippingCost());
        s.setStatus(Shipping.Status.PROCESSING);
        s.setShippedAt(req.shippedAt());
        s.setExpectedDeliveryDate(req.expectedDeliveryDate());
        s.setDeliveredAt(null);
        s.setNotes(req.notes());

        repo.save(s);

        return repo.findByOrderId(order.getId())
                   .stream()
                   .reduce((first, second) -> second)
                   .orElse(s);
    }

    @Transactional
    public Shipping update(Long id, UpdateShipmentRequest req) {
        Shipping s = repo.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("Shipping not found"));

        Shipping.Status oldStatus = s.getStatus();

        s.setDeliveryAddress(req.deliveryAddress());
        s.setCity(req.city());
        s.setPostalCode(req.postalCode());
        s.setCountry(req.country());
        s.setCarrierName(req.carrierName());
        s.setTrackingNumber(req.trackingNumber());
        s.setShippingCost(req.shippingCost());
        s.setStatus(Shipping.Status.valueOf(req.status()));
        s.setShippedAt(req.shippedAt());
        s.setExpectedDeliveryDate(req.expectedDeliveryDate());
        s.setDeliveredAt(req.deliveredAt());
        s.setNotes(req.notes());
        s.setUpdatedAt(LocalDateTime.now());

        repo.update(s);

        jdbc.update("""
            INSERT INTO shipment_status_history (shipment_id, old_status, new_status, notes, changed_at)
            VALUES (?, ?, ?, ?, NOW())
        """, s.getId(), oldStatus.name(), s.getStatus().name(), req.notes());

        return s;
    }

    @Transactional(readOnly = true)
    public Shipping getById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("Shipping not found"));
    }
}