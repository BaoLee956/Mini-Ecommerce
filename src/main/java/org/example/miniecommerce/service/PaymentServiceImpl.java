package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.payment.CreatePaymentRequest;
import org.example.miniecommerce.dto.payment.ConfirmPaymentRequest;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.Payment;
import org.example.miniecommerce.factory.PaymentFactory;
import org.example.miniecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repo;
    private final OrderLookupService orderLookup;

    public PaymentServiceImpl(PaymentRepository repo, OrderLookupService orderLookup) {
        this.repo = repo;
        this.orderLookup = orderLookup;
    }

    @Override
    @Transactional
    public Payment create(CreatePaymentRequest req) {
        Order order = orderLookup.findByIdOrThrow(req.orderId());
        Payment p = PaymentFactory.fromCreateRequest(req, order);

        repo.save(p);

        // Sau khi save, query lại để lấy Payment mới nhất
        return repo.findByOrderId(order.getId())
                   .stream()
                   .reduce((first, second) -> second)
                   .orElse(p);
    }

    @Override
    @Transactional
    public Payment confirm(ConfirmPaymentRequest req) {
        Payment p = repo.findById(req.paymentId())
                        .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (p.getStatus() == Payment.Status.PAID && req.success()) {
            return p;
        }

        PaymentFactory.applyConfirm(p, req.success());

        repo.update(p);

        return p;
    }
}
