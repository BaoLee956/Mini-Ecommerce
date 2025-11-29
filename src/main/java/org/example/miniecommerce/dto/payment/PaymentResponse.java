package org.example.miniecommerce.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    Long id,
    Long orderId,
    BigDecimal amount,
    String paymentMethod,
    String status,
    String failureReason,
    String transactionId,
    LocalDateTime paidAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}