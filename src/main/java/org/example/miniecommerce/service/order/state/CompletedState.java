package org.example.miniecommerce.service.order.state;

import org.example.miniecommerce.entity.Order;

/**
 * Trạng thái COMPLETED: Order đã hoàn thành
 */
public class CompletedState implements OrderState {

    @Override
    public void handlePaymentSuccess(Order order) {
        throw new IllegalStateException("Cannot process payment success for completed order");
    }

    @Override
    public void handleShipmentCreated(Order order) {
        throw new IllegalStateException("Cannot create shipment for completed order");
    }

    @Override
    public void handleShipmentStarted(Order order) {
        throw new IllegalStateException("Cannot start shipment for completed order");
    }

    @Override
    public void handleShipmentDelivered(Order order) {
        throw new IllegalStateException("Order is already completed");
    }

    @Override
    public void handlePaymentFailed(Order order) {
        throw new IllegalStateException("Cannot process payment failure for completed order");
    }

    @Override
    public void handleCancel(Order order) {
        throw new IllegalStateException("Cannot cancel completed order");
    }

    @Override
    public String getStateName() {
        return "COMPLETED";
    }
}
