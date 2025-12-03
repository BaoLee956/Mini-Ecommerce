package org.example.miniecommerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDetailWithProductDto(
    Long orderId,
    Long userId,
    BigDecimal totalAmount,
    String status,
    LocalDateTime createdAt,
    Long itemCount,
    String itemsSummary,
    String inventoryStatus
) {}

