package org.example.miniecommerce.dto.shipping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ShipmentResponse(
    Long id,
    Long orderId,
    String carrierName,
    String trackingNumber,
    String deliveryAddress,
    String city,
    String postalCode,
    String country,
    BigDecimal shippingCost,
    String status,
    LocalDateTime shippedAt,
    LocalDateTime expectedDeliveryDate,
    LocalDateTime deliveredAt,
    String notes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}