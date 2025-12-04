package org.example.miniecommerce.factory;

import org.example.miniecommerce.dto.shipping.CreateShipmentRequest;
import org.example.miniecommerce.dto.shipping.UpdateShipmentRequest;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.OrderStatus;
import org.example.miniecommerce.entity.Shipping;

import java.time.LocalDateTime;

public class ShippingFactory {

    public static Shipping fromCreateRequest(CreateShipmentRequest req, Order order) {
        Shipping s = new Shipping();
        s.setOrder(order);
        s.setOrderId(order.getId());
        s.setCarrierName(req.carrierName());
        s.setTrackingNumber(req.trackingNumber());
        s.setDeliveryAddress(req.address());
        s.setCity(req.city());
        s.setPostalCode(req.postalCode());
        s.setCountry(req.country());
        s.setShippingCost(req.shippingCost());
        s.setExpectedDeliveryDate(req.expectedDeliveryDate());
        s.setNotes(req.notes());
        s.setStatus(Shipping.Status.PROCESSING);
        return s;
    }

    public static void applyUpdate(Shipping s, UpdateShipmentRequest req) {
        if (req.carrierName() != null) s.setCarrierName(req.carrierName());
        if (req.trackingNumber() != null) s.setTrackingNumber(req.trackingNumber());
        if (req.status() != null) {
            Shipping.Status newStatus = Shipping.Status.valueOf(req.status().toUpperCase());
            s.setStatus(newStatus);

            if (newStatus == Shipping.Status.SHIPPED && s.getShippedAt() == null) {
                s.setShippedAt(LocalDateTime.now());
            } else if (newStatus == Shipping.Status.DELIVERED && s.getDeliveredAt() == null) {
                s.setDeliveredAt(LocalDateTime.now());
            }

            if (s.getOrder() != null) {
                if (newStatus == Shipping.Status.SHIPPED || newStatus == Shipping.Status.IN_TRANSIT) {
                    s.getOrder().setStatus(OrderStatus.SHIPPED);
                } else if (newStatus == Shipping.Status.DELIVERED) {
                    s.getOrder().setStatus(OrderStatus.DELIVERED);
                }
            }
        }
        if (req.address() != null) s.setDeliveryAddress(req.address());
        if (req.city() != null) s.setCity(req.city());
        if (req.postalCode() != null) s.setPostalCode(req.postalCode());
        if (req.country() != null) s.setCountry(req.country());
        if (req.shippingCost() != null) s.setShippingCost(req.shippingCost());
        if (req.expectedDeliveryDate() != null) s.setExpectedDeliveryDate(req.expectedDeliveryDate());
        if (req.notes() != null) s.setNotes(req.notes());
    }
}