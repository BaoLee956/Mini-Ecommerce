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
}