package org.example.miniecommerce.mapper;

import org.example.miniecommerce.dto.payment.PaymentResponse;
import org.example.miniecommerce.entity.Payment;

public class PaymentMapper {

    public static PaymentResponse toResponse(Payment p) {
        if (p == null) return null;

        return new PaymentResponse(
            p.getId(),
            p.getOrderId(),
            p.getAmount(),
            p.getPaymentMethod(),
            p.getStatus().name(),
            p.getFailureReason(),
            p.getTransactionId(),
            p.getPaidAt(),
            p.getCreatedAt(),
            p.getUpdatedAt()
        );
    }
}