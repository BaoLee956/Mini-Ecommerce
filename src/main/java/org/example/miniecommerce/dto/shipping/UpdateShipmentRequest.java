package org.example.miniecommerce.dto.shipping;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateShipmentRequest(
    Long shippingId,
    String carrierName,
    String trackingNumber,
    String status,
    String address,
    String city,
    String postalCode,
    String country,
    BigDecimal shippingCost,
    LocalDateTime expectedDeliveryDate,
    String notes
) {}