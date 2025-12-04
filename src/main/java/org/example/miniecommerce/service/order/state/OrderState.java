package org.example.miniecommerce.service.order.state;

import org.example.miniecommerce.entity.Order;

/**
 * State Pattern: Interface cho các trạng thái của Order
 */
public interface OrderState {

    /**
     * Xử lý khi payment được confirm thành công
     */
    void handlePaymentSuccess(Order order);

    /**
     * Xử lý khi tạo shipment
     */
    void handleShipmentCreated(Order order);

    /**
     * Xử lý khi shipment bắt đầu vận chuyển
     */
    void handleShipmentStarted(Order order);

    /**
     * Xử lý khi shipment được delivered
     */
    void handleShipmentDelivered(Order order);

    /**
     * Xử lý khi payment thất bại
     */
    void handlePaymentFailed(Order order);

    /**
     * Xử lý khi order bị cancel
     */
    void handleCancel(Order order);

    /**
     * Lấy tên trạng thái
     */
    String getStateName();
}
