package org.example.miniecommerce.factory;

import org.example.miniecommerce.dto.shipping.CreateShipmentRequest;
import org.example.miniecommerce.dto.shipping.UpdateShipmentRequest;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.OrderStatus;
import org.example.miniecommerce.entity.Shipping;

public class ShippingFactory {

    public static Shipping fromCreateRequest(CreateShipmentRequest req, Order order) {
        Shipping s = new Shipping();
        s.setOrder(order);
        s.setAddress(req.address());
        s.setCity(req.city());
        s.setPostalCode(req.postalCode());
        s.setCountry(req.country());
        s.setStatus(Shipping.Status.PENDING);
        return s;
    }

    public static void applyUpdate(Shipping s, UpdateShipmentRequest req) {
        if (req.status() != null) {
            Shipping.Status newStatus = Shipping.Status.valueOf(req.status());
            s.setStatus(newStatus);
            if (newStatus == Shipping.Status.SHIPPED) {
                s.getOrder().setStatus(OrderStatus.SHIPPED);
            } else if (newStatus == Shipping.Status.DELIVERED) {
                s.getOrder().setStatus(OrderStatus.DELIVERED);
            }
        }
        if (req.address() != null) s.setAddress(req.address());
        if (req.city() != null) s.setCity(req.city());
        if (req.postalCode() != null) s.setPostalCode(req.postalCode());
        if (req.country() != null) s.setCountry(req.country());
    }
}