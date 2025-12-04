package org.example.miniecommerce.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.miniecommerce.dto.order.CreateOrderItemDto;
import org.example.miniecommerce.dto.product.ProductResponse;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.OrderItem;
import org.example.miniecommerce.entity.OrderStatus;
import org.example.miniecommerce.service.ProductService;
import org.example.miniecommerce.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderFactory {

    private final ProductService productService;
    // private final UserService userService;


    public Order createOrder(Long userId, List<CreateOrderItemDto> itemsDto) {
        // Validate input parameters
        if (userId == null) {
            log.error("UserId cannot be null");
            throw new IllegalArgumentException("UserId cannot be null");
        }

        // Validate user exists in database
        // try {
        //     userService.getUserId(String.valueOf(userId));
        //     log.info("User with id {} exists in database", userId);
        // } catch (RuntimeException e) {
        //     log.error("User with id {} does not exist in database", userId);
        //     throw new IllegalArgumentException("User with id " + userId + " does not exist", e);
        // }

        if (itemsDto == null || itemsDto.isEmpty()) {
            log.error("Items list cannot be null or empty");
            throw new IllegalArgumentException("Items list cannot be null or empty");
        }

        log.info("Creating order for userId: {}, with {} items", userId, itemsDto.size());

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderItemDto dto : itemsDto) {
            // Validate order item
            if (dto == null) {
                log.error("Order item cannot be null");
                throw new IllegalArgumentException("Order item cannot be null");
            }
            if (dto.productId() == null) {
                log.error("ProductId cannot be null");
                throw new IllegalArgumentException("ProductId cannot be null");
            }
            if (dto.quantity() == null || dto.quantity() <= 0) {
                log.error("Quantity must be greater than 0 for productId: {}", dto.productId());
                throw new IllegalArgumentException("Quantity must be greater than 0 for productId: " + dto.productId());
            }

            log.info("Processing item - productId: {}, quantity: {}", dto.productId(), dto.quantity());

            ProductResponse product = productService.get(dto.productId());

            // Validate product data
            if (product.price() == null) {
                log.error("Product with id {} has null price", dto.productId());
                throw new IllegalStateException("Product price cannot be null for product ID: " + dto.productId());
            }

            if (product.id() == null) {
                log.error("Product with id {} has null id", dto.productId());
                throw new IllegalStateException("Product id cannot be null for product ID: " + dto.productId());
            }

            OrderItem item = new OrderItem();
            item.setProductId(product.id());
            item.setQuantity(dto.quantity());
            item.setPrice(product.price()); // Lấy từ DB để tránh gian lận
            order.getItems()
                    .add(item);

            BigDecimal itemTotal = product.price()
                    .multiply(BigDecimal.valueOf(dto.quantity()));
            total = total.add(itemTotal);
            log.info("Added item total: {} ({} x {}), running total: {}", itemTotal, product.price(), dto.quantity(),
                    total);
        }

        order.setTotalAmount(total);
        log.info("Order created successfully with total amount: {}", total);
        return order;
    }
}