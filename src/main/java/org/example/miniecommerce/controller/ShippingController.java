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

    // Tạo shipment mới
    @PostMapping
    public ResponseEntity<ShipmentResponse> create(@Valid @RequestBody CreateShipmentRequest req) {
        Shipping s = svc.create(req);
        return ResponseEntity.status(201).body(ShippingMapper.toResponse(s));
    }

    // Cập nhật shipment theo id
    @PatchMapping("/{id}")
    public ResponseEntity<ShipmentResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateShipmentRequest req) {
        Shipping s = svc.update(id, req);
        return ResponseEntity.ok(ShippingMapper.toResponse(s));
    }

    // Lấy shipment theo id
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getById(@PathVariable Long id) {
        Shipping s = svc.getById(id);
        return ResponseEntity.ok(ShippingMapper.toResponse(s));
    }
}