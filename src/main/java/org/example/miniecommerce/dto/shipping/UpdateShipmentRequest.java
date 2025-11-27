package org.example.miniecommerce.dto.shipping;
public record UpdateShipmentRequest(
    Long shippingId,
    String status,
    String address,
    String city,
    String postalCode,
    String country
) {}