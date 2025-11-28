package org.example.miniecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "shipments")
@SQLDelete(sql = "UPDATE shipments SET deleted_at = NOW() WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class Shipping extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    private String city;
    private String postalCode;
    private String country;

    @Column(name = "carrier_name", nullable = false)
    private String carrierName;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipping_cost", precision = 12, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "notes")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PROCESSING;

    public enum Status {
        PROCESSING, SHIPPED, IN_TRANSIT, DELIVERED, LOST, DAMAGED, CANCELLED
    }

    @Transient
    public Long getOrderId() {
        return (order != null) ? order.getId() : null;
    }
}