package org.example.miniecommerce.service.order;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.dto.order.*;
import org.example.miniecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderAnalyticsServiceImpl implements OrderAnalyticsService {

    private final OrderRepository orderRepository;

    @Override
    public List<MonthlyRevenueDto> getMonthlyRevenueStatistics(LocalDate startDate, LocalDate endDate, String status) {
        return orderRepository.getMonthlyRevenueStatistics(startDate, endDate, status);
    }

    @Override
    public List<TopProductDto> getTopSellingProducts(LocalDate startDate, LocalDate endDate, Long minQuantity, int limit) {
        return orderRepository.getTopSellingProducts(startDate, endDate, minQuantity, limit);
    }

    @Override
    public List<CustomerAnalyticsDto> getCustomerAnalytics(Long minOrders) {
        return orderRepository.getCustomerAnalytics(minOrders);
    }

    @Override
    public List<OrderDetailWithProductDto> getUserOrdersWithProductInfo(Long userId) {
        return orderRepository.getOrdersWithProductInfo(userId);
    }

    @Override
    public Long getTotalOrdersByStatusInPeriod(String status, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.getTotalOrdersByStatusInPeriod(status, startDate, endDate);
    }

    @Override
    public Double getAverageOrderValueByStatus(String status) {
        return orderRepository.getAverageOrderValueByStatus(status);
    }
}
