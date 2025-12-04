package org.example.miniecommerce.service;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.dto.shipping.CreateShipmentRequest;
import org.example.miniecommerce.dto.shipping.UpdateShipmentRequest;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.Shipping;
import org.example.miniecommerce.factory.ShippingFactory;
import org.example.miniecommerce.repository.ShippingRepository;
import org.example.miniecommerce.service.order.OrderService;
import org.example.miniecommerce.service.order.decorator.OrderDecoratorName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private final ShippingRepository repo;
    private final OrderLookupService orderLookup;
    private final OrderService orderService;


    @Override
    @Transactional
    public Shipping create(CreateShipmentRequest req) {
        Order order = orderLookup.findByIdOrThrow(req.orderId());
        Shipping s = ShippingFactory.fromCreateRequest(req, order);
        // Cập nhật giá phí lên order
        orderService.addFee(req.orderId(), OrderDecoratorName.SHIPPING, req.shippingCost());
        orderService.handleShipmentCreated(order.getId());
        repo.save(s);

        // Sau khi save, query lại để lấy Shipping mới nhất
        return repo.findByOrderId(order.getId())
                .stream()
                .reduce((first, second) -> second)
                .orElse(s);
    }

    @Override
    @Transactional
    public Shipping update(Long id, UpdateShipmentRequest req) {
        Shipping s = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Shipping not found"));

        ShippingFactory.applyUpdate(s, req);

        switch (s.getStatus()) {
            case IN_TRANSIT:
                orderService.handleShipmentStarted(s.getOrderId());
                break;
            case DELIVERED:
                orderService.handleShipmentDelivered(s.getOrderId());
                break;
            default:
                // No action needed for other shipping statuses
                break;
        }

        repo.update(s);

        return s;
    }
}
