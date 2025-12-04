package org.example.miniecommerce.service.order.state;

import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.OrderStatus;

/**
 * Trạng thái PENDING_PAYMENT: Đang chờ thanh toán
 */
public class PendingPaymentState implements OrderState {

    @Override
    public void handlePaymentSuccess(Order order) {
        order.setStatus(OrderStatus.PENDING_SHIPMENT);
        order.setState(new PendingShipmentState());
    }

    @Override
    public void handleShipmentCreated(Order order) {
        throw new IllegalStateException("Cannot create shipment for order in PENDING_PAYMENT state");
    }

    @Override
    public void handleShipmentStarted(Order order) {
        throw new IllegalStateException("Cannot start shipment for order in PENDING_PAYMENT state - payment not completed");
    }

    @Override
    public void handleShipmentDelivered(Order order) {
        throw new IllegalStateException("Cannot deliver shipment for order in PENDING_PAYMENT state");
    }

    @Override
    public void handlePaymentFailed(Order order) {
        // Có thể chuyển về CREATED để cho phép thử lại payment
        order.setStatus(OrderStatus.CREATED);
        order.setState(new CreatedState());
    }

    @Override
    public void handleCancel(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "PENDING_PAYMENT";
    }
}
