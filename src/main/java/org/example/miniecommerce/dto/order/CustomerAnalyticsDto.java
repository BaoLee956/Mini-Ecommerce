package org.example.miniecommerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerAnalyticsDto(
    Long userId,
    String userName,
    Long totalOrders,
    BigDecimal lifetimeValue,
    BigDecimal avgOrderValue,
    LocalDateTime lastOrderDate,
    LocalDateTime firstOrderDate,
    Long customerLifetimeDays
) {}

