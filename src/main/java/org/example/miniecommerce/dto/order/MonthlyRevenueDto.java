package org.example.miniecommerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MonthlyRevenueDto(
    LocalDateTime period,
    BigDecimal totalRevenue,
    Long totalOrders,
    BigDecimal avgOrderValue,
    Long uniqueCustomers
) {}

