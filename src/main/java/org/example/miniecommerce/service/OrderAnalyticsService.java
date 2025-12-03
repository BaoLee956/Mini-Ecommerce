package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.order.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderAnalyticsService {
    List<MonthlyRevenueDto> getMonthlyRevenueStatistics(LocalDate startDate, LocalDate endDate, String status);
    List<TopProductDto> getTopSellingProducts(LocalDate startDate, LocalDate endDate, Long minQuantity, int limit);
    List<CustomerAnalyticsDto> getCustomerAnalytics(Long minOrders);
    List<OrderDetailWithProductDto> getUserOrdersWithProductInfo(Long userId);
    Long getTotalOrdersByStatusInPeriod(String status, LocalDateTime startDate, LocalDateTime endDate);
    Double getAverageOrderValueByStatus(String status);
}
