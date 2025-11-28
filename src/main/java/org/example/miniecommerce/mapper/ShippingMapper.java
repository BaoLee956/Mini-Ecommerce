package org.example.miniecommerce.mapper;

import org.example.miniecommerce.dto.shipping.ShipmentResponse;
import org.example.miniecommerce.entity.Shipping;

public class ShippingMapper {

    public static ShipmentResponse toResponse(Shipping s) {
        if (s == null) return null;

        return new ShipmentResponse(
            s.getId(),
            s.getOrderId(),
            s.getDeliveryAddress(),
            s.getCity(),
            s.getPostalCode(),
            s.getCountry(),
            s.getCarrierName(),
            s.getTrackingNumber(),
            s.getShippingCost(),
            s.getStatus().name(),
            s.getShippedAt(),
            s.getExpectedDeliveryDate(),
            s.getDeliveredAt(),
            s.getNotes(),
            s.getCreatedAt(),
            s.getUpdatedAt()
        );
    }
}