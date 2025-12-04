package org.example.miniecommerce.repository;

import org.example.miniecommerce.dto.order.*;
import org.example.miniecommerce.entity.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(Long id);

    List<Order> findByUserId(Long userId);

    List<Order> findAll();

    Order save(Order order);

    void deleteById(Long id);

    // Analytics methods
    List<MonthlyRevenueDto> getMonthlyRevenueStatistics(LocalDate startDate, LocalDate endDate, String status);

    List<TopProductDto> getTopSellingProducts(LocalDate startDate, LocalDate endDate, Long minQuantity, int limit);

    List<CustomerAnalyticsDto> getCustomerAnalytics(Long minOrders);

    List<OrderDetailWithProductDto> getOrdersWithProductInfo(Long userId);

    Long getTotalOrdersByStatusInPeriod(String status, LocalDateTime startDate, LocalDateTime endDate);

    Double getAverageOrderValueByStatus(String status);
}
