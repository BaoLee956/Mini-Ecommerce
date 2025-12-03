package org.example.miniecommerce.factory;

import org.example.miniecommerce.dto.payment.CreatePaymentRequest;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.OrderStatus;
import org.example.miniecommerce.entity.Payment;

import java.time.LocalDateTime;

public class PaymentFactory {
    public static Payment fromCreateRequest(CreatePaymentRequest req, Order order) {
        Payment p = new Payment();
        p.setOrder(order);
        p.setAmount(req.amount());
        p.setPaymentMethod(req.method());
        p.setStatus(Payment.Status.PENDING);
        return p;
    }

    public static void applyConfirm(Payment p, boolean success) {
        if (success) {
            p.setStatus(Payment.Status.SUCCESS);
            p.setPaidAt(LocalDateTime.now());
            if (p.getOrder() != null) {
                p.getOrder().setStatus(OrderStatus.PAID);
            }
        } else {
            p.setStatus(Payment.Status.FAILED);
        }
    }
}