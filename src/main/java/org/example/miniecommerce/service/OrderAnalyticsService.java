package org.example.miniecommerce.service;

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
public class OrderAnalyticsService {

    private final OrderRepository orderRepository;

    /**
     * Lấy thống kê doanh thu theo tháng
     *
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @param status Trạng thái order (VD: COMPLETED, PAID)
     * @return Danh sách thống kê doanh thu theo tháng
     */
    public List<MonthlyRevenueDto> getMonthlyRevenueStatistics(LocalDate startDate, LocalDate endDate, String status) {
        return orderRepository.getMonthlyRevenueStatistics(startDate, endDate, status);
    }

    /**
     * Lấy top sản phẩm bán chạy nhất
     *
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @param minQuantity Số lượng tối thiểu đã bán
     * @param limit Số lượng top products (VD: 10)
     * @return Danh sách top products
     */
    public List<TopProductDto> getTopSellingProducts(LocalDate startDate, LocalDate endDate, Long minQuantity, int limit) {
        return orderRepository.getTopSellingProducts(startDate, endDate, minQuantity, limit);
    }

    /**
     * Lấy phân tích khách hàng
     *
     * @param minOrders Số order tối thiểu để được tính vào
     * @return Danh sách analytics khách hàng
     */
    public List<CustomerAnalyticsDto> getCustomerAnalytics(Long minOrders) {
        return orderRepository.getCustomerAnalytics(minOrders);
    }

    /**
     * Lấy chi tiết orders của một user với thông tin sản phẩm
     *
     * @param userId ID của user
     * @return Danh sách orders với chi tiết sản phẩm
     */
    public List<OrderDetailWithProductDto> getUserOrdersWithProductInfo(Long userId) {
        return orderRepository.getOrdersWithProductInfo(userId);
    }

    /**
     * Lấy tổng số orders theo trạng thái trong khoảng thời gian
     *
     * @param status Trạng thái order
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return Tổng số orders
     */
    public Long getTotalOrdersByStatusInPeriod(String status, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.getTotalOrdersByStatusInPeriod(status, startDate, endDate);
    }

    /**
     * Lấy giá trị đơn hàng trung bình theo trạng thái
     *
     * @param status Trạng thái order
     * @return Giá trị trung bình
     */
    public Double getAverageOrderValueByStatus(String status) {
        return orderRepository.getAverageOrderValueByStatus(status);
    }
}

