package org.example.miniecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.miniecommerce.service.order.state.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = NOW() WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class Order extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @Transient
    private OrderState state;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Khởi tạo state dựa trên status hiện tại
     */
    @PostLoad
    private void initializeState() {
        switch (this.status) {
            case CREATED -> this.state = new CreatedState();
            case PENDING_PAYMENT -> this.state = new PendingPaymentState();
            case PENDING_SHIPMENT -> this.state = new PendingShipmentState();
            case COMPLETED -> this.state = new CompletedState();
            case CANCELLED -> this.state = new CancelledState();
            default -> throw new IllegalStateException("Unknown order status: " + this.status);
        }
    }

    /**
     * Delegate các hành động đến state hiện tại
     */
    public void handlePaymentSuccess() {
        if (state == null) initializeState();
        state.handlePaymentSuccess(this);
    }

    public void handleShipmentCreated() {
        if (state == null) initializeState();
        state.handleShipmentCreated(this);
    }

    public void handleShipmentStarted() {
        if (state == null) initializeState();
        state.handleShipmentStarted(this);
    }

    public void handleShipmentDelivered() {
        if (state == null) initializeState();
        state.handleShipmentDelivered(this);
    }

    public void handlePaymentFailed() {
        if (state == null) initializeState();
        state.handlePaymentFailed(this);
    }

    public void handleCancel() {
        if (state == null) initializeState();
        state.handleCancel(this);
    }

    public String getStateName() {
        if (state == null) initializeState();
        return state.getStateName();
    }

}
