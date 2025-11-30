package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.payment.CreatePaymentRequest;
import org.example.miniecommerce.dto.payment.ConfirmPaymentRequest;
import org.example.miniecommerce.entity.Payment;

public interface PaymentService {
    Payment create(CreatePaymentRequest req);
    Payment confirm(ConfirmPaymentRequest req);
}