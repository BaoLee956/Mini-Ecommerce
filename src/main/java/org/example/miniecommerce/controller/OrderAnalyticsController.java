package org.example.miniecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.dto.order.*;
import org.example.miniecommerce.service.order.OrderAnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders/analytics")
@RequiredArgsConstructor
public class OrderAnalyticsController {

    private final OrderAnalyticsService orderAnalyticsService;

    /**
     * Lấy thống kê doanh thu theo tháng
     * GET /api/orders/analytics/monthly-revenue?startDate=2024-01-01&endDate=2024-12-31&status=COMPLETED
     */
    @GetMapping("/monthly-revenue")
    public ResponseEntity<List<MonthlyRevenueDto>> getMonthlyRevenueStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "COMPLETED") String status) {
        List<MonthlyRevenueDto> statistics = orderAnalyticsService.getMonthlyRevenueStatistics(startDate, endDate, status);
        return ResponseEntity.ok(statistics);
    }

    /**
     * Lấy top products bán chạy nhất
     * GET /api/orders/analytics/top-products?startDate=2024-01-01&endDate=2024-12-31&minQuantity=5&limit=10
     */
    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDto>> getTopSellingProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") Long minQuantity,
            @RequestParam(defaultValue = "10") int limit) {
        List<TopProductDto> topProducts = orderAnalyticsService.getTopSellingProducts(startDate, endDate, minQuantity, limit);
        return ResponseEntity.ok(topProducts);
    }

    /**
     * Lấy phân tích khách hàng
     * GET /api/orders/analytics/customer-analytics?minOrders=2
     */
    @GetMapping("/customer-analytics")
    public ResponseEntity<List<CustomerAnalyticsDto>> getCustomerAnalytics(
            @RequestParam(defaultValue = "0") Long minOrders) {
        List<CustomerAnalyticsDto> analytics = orderAnalyticsService.getCustomerAnalytics(minOrders);
        return ResponseEntity.ok(analytics);
    }

    /**
     * Lấy chi tiết orders của một user với thông tin sản phẩm
     * GET /api/orders/analytics/user-orders/{userId}
     */
    @GetMapping("/user-orders/{userId}")
    public ResponseEntity<List<OrderDetailWithProductDto>> getUserOrdersWithProductInfo(
            @PathVariable Long userId) {
        List<OrderDetailWithProductDto> orders = orderAnalyticsService.getUserOrdersWithProductInfo(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Lấy tổng số orders theo trạng thái trong khoảng thời gian
     * GET /api/orders/analytics/total-by-status?status=COMPLETED&startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
     */
    @GetMapping("/total-by-status")
    public ResponseEntity<Long> getTotalOrdersByStatusInPeriod(
            @RequestParam String status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long total = orderAnalyticsService.getTotalOrdersByStatusInPeriod(status, startDate, endDate);
        return ResponseEntity.ok(total);
    }

    /**
     * Lấy giá trị đơn hàng trung bình theo trạng thái
     * GET /api/orders/analytics/average-value?status=COMPLETED
     */
    @GetMapping("/average-value")
    public ResponseEntity<Double> getAverageOrderValueByStatus(
            @RequestParam(defaultValue = "COMPLETED") String status) {
        Double avgValue = orderAnalyticsService.getAverageOrderValueByStatus(status);
        return ResponseEntity.ok(avgValue);
    }
}

