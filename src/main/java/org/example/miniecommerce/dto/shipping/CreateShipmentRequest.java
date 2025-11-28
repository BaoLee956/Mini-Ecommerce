package org.example.miniecommerce.dto.shipping;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateShipmentRequest(
    @NotNull Long orderId,
    @NotBlank String deliveryAddress,
    @NotBlank String city,
    String postalCode,
    @NotBlank String country,
    @NotBlank String carrierName,
    String trackingNumber,
    @DecimalMin(value = "0.0") BigDecimal shippingCost,
    LocalDateTime shippedAt,
    LocalDateTime expectedDeliveryDate,
    String notes
) {}