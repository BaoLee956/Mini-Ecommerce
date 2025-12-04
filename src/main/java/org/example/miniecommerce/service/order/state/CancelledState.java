package org.example.miniecommerce.service.order.state;

import org.example.miniecommerce.entity.Order;

/**
 * Trạng thái CANCELLED: Order đã bị hủy
 */
public class CancelledState implements OrderState {

    @Override
    public void handlePaymentSuccess(Order order) {
        throw new IllegalStateException("Cannot process payment success for cancelled order");
    }

    @Override
    public void handleShipmentCreated(Order order) {
        throw new IllegalStateException("Cannot create shipment for cancelled order");
    }

    @Override
    public void handleShipmentStarted(Order order) {
        throw new IllegalStateException("Cannot start shipment for cancelled order");
    }

    @Override
    public void handleShipmentDelivered(Order order) {
        throw new IllegalStateException("Cannot deliver shipment for cancelled order");
    }

    @Override
    public void handlePaymentFailed(Order order) {
        throw new IllegalStateException("Cannot process payment failure for cancelled order");
    }

    @Override
    public void handleCancel(Order order) {
        throw new IllegalStateException("Order is already cancelled");
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}
