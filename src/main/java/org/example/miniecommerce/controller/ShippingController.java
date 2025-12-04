package org.example.miniecommerce.controller;

import org.example.miniecommerce.dto.shipping.CreateShipmentRequest;
import org.example.miniecommerce.dto.shipping.ShipmentResponse;
import org.example.miniecommerce.dto.shipping.UpdateShipmentRequest;
import org.example.miniecommerce.entity.Shipping;
import org.example.miniecommerce.mapper.ShippingMapper;
import org.example.miniecommerce.service.ShippingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shipments")
public class ShippingController {

    private final ShippingService svc;

    public ShippingController(ShippingService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> create(@Valid @RequestBody CreateShipmentRequest req) {
        Shipping s = svc.create(req);
        return ResponseEntity.status(201).body(ShippingMapper.toResponse(s));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShipmentResponse> update(@PathVariable Long id,
                                                   @RequestBody UpdateShipmentRequest req) {
        UpdateShipmentRequest updatedReq = new UpdateShipmentRequest(
            id,
            req.carrierName(),
            req.trackingNumber(),
            req.status(),
            req.address(),
            req.city(),
            req.postalCode(),
            req.country(),
            req.shippingCost(),
            req.expectedDeliveryDate(),
            req.notes()
        );
        Shipping s = svc.update(updatedReq);
        return ResponseEntity.ok(ShippingMapper.toResponse(s));
    }

    // Lấy tất cả shipments
    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAll() {
        List<Shipping> shipments = svc.getAll();
        List<ShipmentResponse> responses = shipments.stream()
                .map(ShippingMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Lấy shipment theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getById(@PathVariable Long id) {
        Shipping shipping = svc.getById(id);
        return ResponseEntity.ok(ShippingMapper.toResponse(shipping));
    }

    // Lấy shipments theo order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ShipmentResponse>> getByOrderId(@PathVariable Long orderId) {
        List<Shipping> shipments = svc.getByOrderId(orderId);
        List<ShipmentResponse> responses = shipments.stream()
                .map(ShippingMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Lấy shipments theo status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShipmentResponse>> getByStatus(@PathVariable String status) {
        try {
            Shipping.Status shippingStatus = Shipping.Status.valueOf(status.toUpperCase());
            List<Shipping> shipments = svc.getByStatus(shippingStatus);
            List<ShipmentResponse> responses = shipments.stream()
                    .map(ShippingMapper::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Xóa shipment theo ID (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        svc.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Đếm số lượng shipments
    @GetMapping("/count")
    public ResponseEntity<Integer> count() {
        int count = svc.count();
        return ResponseEntity.ok(count);
    }

    // Đếm shipments theo status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Integer> countByStatus(@PathVariable String status) {
        try {
            Shipping.Status shippingStatus = Shipping.Status.valueOf(status.toUpperCase());
            int count = svc.countByStatus(shippingStatus);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}