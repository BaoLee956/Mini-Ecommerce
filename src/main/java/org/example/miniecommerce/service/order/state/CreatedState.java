package org.example.miniecommerce.service.order.state;

import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.OrderStatus;

/**
 * Trạng thái CREATED: Order vừa được tạo
 */
public class CreatedState implements OrderState {

    @Override
    public void handlePaymentSuccess(Order order) {
        throw new IllegalStateException("Cannot process payment success for order in CREATED state");
    }

    @Override
    public void handleShipmentCreated(Order order) {
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setState(new PendingPaymentState());
    }

    @Override
    public void handleShipmentStarted(Order order) {
        throw new IllegalStateException("Cannot start shipment for order in CREATED state");
    }

    @Override
    public void handleShipmentDelivered(Order order) {
        throw new IllegalStateException("Cannot deliver shipment for order in CREATED state");
    }

    @Override
    public void handlePaymentFailed(Order order) {
        throw new IllegalStateException("Cannot process payment failure for order in CREATED state");
    }

    @Override
    public void handleCancel(Order order) {
        order.setStatus(OrderStatus.CANCELLED);
        order.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "CREATED";
    }
}
