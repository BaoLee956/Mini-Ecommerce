package org.example.miniecommerce.service.order.state;

import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.OrderStatus;

/**
 * Trạng thái PENDING_SHIPMENT: Đang chờ giao hàng
 */
public class PendingShipmentState implements OrderState {

    @Override
    public void handlePaymentSuccess(Order order) {
        throw new IllegalStateException("Cannot process payment success for order in PENDING_SHIPMENT state");
    }

    @Override
    public void handleShipmentCreated(Order order) {
        throw new IllegalStateException("Cannot create shipment for order in PENDING_SHIPMENT state - shipment already exists");
    }

    @Override
    public void handleShipmentStarted(Order order) {
        // Already in PENDING_SHIPMENT state, no action needed
    }

    @Override
    public void handleShipmentDelivered(Order order) {
        order.setStatus(OrderStatus.COMPLETED);
        order.setState(new CompletedState());
    }

    @Override
    public void handlePaymentFailed(Order order) {
        throw new IllegalStateException("Cannot process payment failure for order in PENDING_SHIPMENT state");
    }

    @Override
    public void handleCancel(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "PENDING_SHIPMENT";
    }
}
