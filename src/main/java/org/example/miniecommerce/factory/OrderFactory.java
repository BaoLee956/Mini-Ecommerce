package org.example.miniecommerce.factory;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.dto.order.CreateOrderItemDto;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.dto.product.ProductResponse;

import org.example.miniecommerce.entity.OrderItem;
import org.example.miniecommerce.entity.OrderStatus;

import org.example.miniecommerce.service.ProductService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFactory {

    private final ProductService productService;


    public Order createOrder(Long userId, List<CreateOrderItemDto> itemsDto) {
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderItemDto dto : itemsDto) {
            ProductResponse product = productService.get(dto.productId());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.id());
            item.setQuantity(dto.quantity());
            item.setPrice(product.price()); // Lấy từ DB để tránh gian lận
            order.getItems().add(item);
            total = total.add(product.price().multiply(BigDecimal.valueOf(dto.quantity())));
        }

        order.setTotalAmount(total);
        return order;
    }
}