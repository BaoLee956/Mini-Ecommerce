package org.example.miniecommerce.mapper;

import org.example.miniecommerce.dto.shipping.ShipmentResponse;
import org.example.miniecommerce.entity.Shipping;

public class ShippingMapper {
    public static ShipmentResponse toResponse(Shipping s) {
        return new ShipmentResponse(
                s.getId(),
                s.getOrderId(),
                s.getCarrierName(),
                s.getTrackingNumber(),
                s.getDeliveryAddress(),
                s.getCity(),
                s.getPostalCode(),
                s.getCountry(),
                s.getShippingCost(),
                s.getStatus().name().toLowerCase(),
                s.getShippedAt(),
                s.getExpectedDeliveryDate(),
                s.getDeliveredAt(),
                s.getNotes(),
                s.getCreatedAt(),
                s.getUpdatedAt()
        );
    }
}