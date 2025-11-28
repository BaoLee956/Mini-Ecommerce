package org.example.miniecommerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderRevenueStatisticsDto(Long uniqueCustomers, BigDecimal avgOrderValue, BigDecimal revenue,
                                        Long totalOrders, LocalDate period) {
}


