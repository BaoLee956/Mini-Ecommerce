package org.example.miniecommerce.factory;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.dto.order.CreateOrderItemDto;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.Product;
import org.example.miniecommerce.entity.OrderItem;
import org.example.miniecommerce.entity.OrderStatus;

import org.example.miniecommerce.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFactory {

    private final ProductRepository productRepository;


    public Order createOrder(Long userId, List<CreateOrderItemDto> itemsDto) {
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderItemDto dto : itemsDto) {
            Product product = productRepository.findById(dto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.productId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setQuantity(dto.quantity());
            item.setPrice(product.getPrice()); // Lấy từ DB để tránh gian lận
            order.getItems().add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(dto.quantity())));
        }

        order.setTotalAmount(total);
        return order;
    }
}