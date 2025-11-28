package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.payment.CreatePaymentRequest;
import org.example.miniecommerce.dto.payment.ConfirmPaymentRequest;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.Payment;
import org.example.miniecommerce.repository.PaymentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final OrderLookupService orderLookup;
    private final JdbcTemplate jdbc;

    public PaymentService(PaymentRepository repo, OrderLookupService orderLookup, JdbcTemplate jdbc) {
        this.repo = repo;
        this.orderLookup = orderLookup;
        this.jdbc = jdbc;
    }

    @Transactional
    public Payment create(CreatePaymentRequest req) {
        Order order = orderLookup.findByIdOrThrow(req.orderId());

        Payment p = new Payment();
        p.setOrder(order);
        p.setAmount(req.amount());
        p.setPaymentMethod(req.method());
        p.setTransactionId(req.transactionId());
        p.setStatus(Payment.Status.PENDING);
        p.setFailureReason(null);
        p.setPaidAt(null);

        repo.save(p);

        return repo.findByOrderId(order.getId())
                   .stream()
                   .reduce((first, second) -> second)
                   .orElse(p);
    }

    @Transactional
    public Payment confirm(ConfirmPaymentRequest req) {
        Payment p = repo.findById(req.paymentId())
                        .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        Payment.Status oldStatus = p.getStatus();

        if (req.success()) {
            p.setStatus(Payment.Status.SUCCESS);
            p.setFailureReason(null);
        } else {
            p.setStatus(Payment.Status.FAILED);
            p.setFailureReason(req.failureReason());
        }

        p.setPaidAt(LocalDateTime.now());
        repo.update(p);

        jdbc.update("""
            INSERT INTO payment_status_history (payment_id, old_status, new_status, reason, changed_at)
            VALUES (?, ?, ?, ?, NOW())
        """, p.getId(), oldStatus.name(), p.getStatus().name(),
             req.success() ? "Confirmed success" : "Failed: " + req.failureReason());

        return p;
    }

    @Transactional(readOnly = true)
    public Payment getById(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }
}