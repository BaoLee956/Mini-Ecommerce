package org.example.miniecommerce.controller;

import org.example.miniecommerce.dto.payment.CreatePaymentRequest;
import org.example.miniecommerce.dto.payment.ConfirmPaymentRequest;
import org.example.miniecommerce.dto.payment.PaymentResponse;
import org.example.miniecommerce.entity.Payment;
import org.example.miniecommerce.mapper.PaymentMapper;
import org.example.miniecommerce.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService svc;

    public PaymentController(PaymentService svc) {
        this.svc = svc;
    }

    // Tạo payment mới cho một order
    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest req) {
        Payment p = svc.create(req);
        return ResponseEntity.status(201).body(PaymentMapper.toResponse(p));
    }

    // Xác nhận payment (ví dụ sau khi provider callback)
    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirm(@Valid @RequestBody ConfirmPaymentRequest req) {
        Payment p = svc.confirm(req);
        return ResponseEntity.ok(PaymentMapper.toResponse(p));
    }

    // Lấy tất cả payments
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {
        List<Payment> payments = svc.getAll();
        List<PaymentResponse> responses = payments.stream()
                .map(PaymentMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Lấy payment theo ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        Payment payment = svc.getById(id);
        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }

    // Lấy payments theo order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getByOrderId(@PathVariable Long orderId) {
        List<Payment> payments = svc.getByOrderId(orderId);
        List<PaymentResponse> responses = payments.stream()
                .map(PaymentMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Lấy payments theo status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getByStatus(@PathVariable String status) {
        try {
            Payment.Status paymentStatus = Payment.Status.valueOf(status.toUpperCase());
            List<Payment> payments = svc.getByStatus(paymentStatus);
            List<PaymentResponse> responses = payments.stream()
                    .map(PaymentMapper::toResponse)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Xóa payment theo ID (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        svc.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Đếm số lượng payments
    @GetMapping("/count")
    public ResponseEntity<Integer> count() {
        int count = svc.count();
        return ResponseEntity.ok(count);
    }

    // Đếm payments theo status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Integer> countByStatus(@PathVariable String status) {
        try {
            Payment.Status paymentStatus = Payment.Status.valueOf(status.toUpperCase());
            int count = svc.countByStatus(paymentStatus);
            return ResponseEntity.ok(count);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}