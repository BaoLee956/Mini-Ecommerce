package org.example.miniecommerce.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
    @NotNull Long orderId,
    @NotBlank String method
) {}