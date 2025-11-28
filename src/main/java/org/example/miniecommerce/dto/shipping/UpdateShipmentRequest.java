package org.example.miniecommerce.dto.shipping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateShipmentRequest(
    Long shippingId,
    String deliveryAddress,
    String city,
    String postalCode,
    String country,
    String carrierName,
    String trackingNumber,
    BigDecimal shippingCost,
    String status,
    LocalDateTime shippedAt,
    LocalDateTime expectedDeliveryDate,
    LocalDateTime deliveredAt,
    String notes
) {}