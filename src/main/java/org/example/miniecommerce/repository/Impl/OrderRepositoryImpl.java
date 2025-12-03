package org.example.miniecommerce.repository.Impl;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.dto.order.CustomerAnalyticsDto;
import org.example.miniecommerce.dto.order.MonthlyRevenueDto;
import org.example.miniecommerce.dto.order.OrderDetailWithProductDto;
import org.example.miniecommerce.dto.order.TopProductDto;
import org.example.miniecommerce.entity.Order;
import org.example.miniecommerce.entity.OrderStatus;
import org.example.miniecommerce.repository.OrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> orderRowMapper = new RowMapper<>() {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setUserId(rs.getLong("user_id"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            order.setStatus(OrderStatus.valueOf(rs.getString("status")));
            order.setCreatedAt(rs.getTimestamp("created_at")
                    .toLocalDateTime());
            order.setUpdatedAt(rs.getTimestamp("updated_at")
                    .toLocalDateTime());

            java.sql.Timestamp deletedAtTimestamp = rs.getTimestamp("deleted_at");
            if (deletedAtTimestamp != null) {
                order.setDeletedAt(deletedAtTimestamp.toLocalDateTime());
            }

            return order;
        }
    };

    private final RowMapper<MonthlyRevenueDto> monthlyRevenueMapper = (rs, rowNum) -> new MonthlyRevenueDto(
            rs.getTimestamp("period")
                    .toLocalDateTime(), rs.getBigDecimal("total_revenue"), rs.getLong("total_orders"),
            rs.getBigDecimal("avg_order_value"), rs.getLong("unique_customers"));

    private final RowMapper<TopProductDto> topProductMapper = (rs, rowNum) -> new TopProductDto(
            rs.getLong("product_id"), rs.getString("product_name"), rs.getLong("times_sold"),
            rs.getLong("total_quantity"), rs.getBigDecimal("total_revenue"), rs.getBigDecimal("avg_price"));

    private final RowMapper<CustomerAnalyticsDto> customerAnalyticsMapper = (rs, rowNum) -> new CustomerAnalyticsDto(
            rs.getLong("user_id"), rs.getString("user_name"), rs.getLong("total_orders"),
            rs.getBigDecimal("lifetime_value"), rs.getBigDecimal("avg_order_value"),
            rs.getTimestamp("last_order_date") != null ? rs.getTimestamp("last_order_date")
                    .toLocalDateTime() : null,
            rs.getTimestamp("first_order_date") != null ? rs.getTimestamp("first_order_date")
                    .toLocalDateTime() : null, rs.getLong("customer_lifetime_days"));

    private final RowMapper<OrderDetailWithProductDto> orderDetailMapper = (rs, rowNum) -> new OrderDetailWithProductDto(
            rs.getLong("order_id"), rs.getLong("user_id"), rs.getBigDecimal("total_amount"), rs.getString("status"),
            rs.getTimestamp("created_at")
                    .toLocalDateTime(), rs.getLong("item_count"), rs.getString("items_summary"),
            rs.getString("inventory_status"));

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ? AND deleted_at IS NULL";
        List<Order> result = jdbcTemplate.query(sql, orderRowMapper, id);
        return result.stream()
                .findFirst();
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? AND deleted_at IS NULL";
        return jdbcTemplate.query(sql, orderRowMapper, userId);
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders WHERE deleted_at IS NULL";
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    @Override
    public Order save(Order order) {
        LocalDateTime now = LocalDateTime.now();

        String sql;
        if (order.getId() == null) {
            sql = "INSERT INTO orders (user_id, total_amount, status, created_at) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setLong(1, order.getUserId());
                ps.setBigDecimal(2, order.getTotalAmount());
                ps.setString(3, order.getStatus()
                        .name());
                ps.setObject(4, now);
                return ps;
            }, keyHolder);

            Long generatedId = keyHolder.getKey()
                    .longValue();
            order.setId(generatedId);
            order.setCreatedAt(now);
        } else {
            sql = "UPDATE orders SET user_id = ?, total_amount = ?, status = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, order.getUserId(), order.getTotalAmount(), order.getStatus()
                    .name(), now, order.getId());
            order.setUpdatedAt(now);
        }
        return order;
    }

    @Override
    public void deleteById(Long id) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "UPDATE orders SET deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, now, id);
    }

    @Override
    public List<MonthlyRevenueDto> getMonthlyRevenueStatistics(LocalDate startDate, LocalDate endDate, String status) {
        String sql = """
                SELECT 
                    DATE_FORMAT(o.created_at, '%Y-%m-01') AS period,
                    SUM(o.total_amount) AS total_revenue,
                    COUNT(o.id) AS total_orders,
                    AVG(o.total_amount) AS avg_order_value,
                    COUNT(DISTINCT o.user_id) AS unique_customers
                FROM orders o
                WHERE o.deleted_at IS NULL 
                    AND o.status = ?
                    AND DATE(o.created_at) BETWEEN ? AND ?
                GROUP BY DATE_FORMAT(o.created_at, '%Y-%m-01')
                ORDER BY period DESC
                """;
        return jdbcTemplate.query(sql, monthlyRevenueMapper, status, startDate, endDate);
    }

    @Override
    public List<TopProductDto> getTopSellingProducts(LocalDate startDate, LocalDate endDate, Long minQuantity,
            int limit) {
        String sql = """
                SELECT 
                    p.id AS product_id,
                    p.name AS product_name,
                    COUNT(oi.id) AS times_sold,
                    SUM(oi.quantity) AS total_quantity,
                    SUM(oi.quantity * oi.unit_price) AS total_revenue,
                    AVG(oi.unit_price) AS avg_price
                FROM orders o
                INNER JOIN order_items oi ON o.id = oi.order_id AND oi.deleted_at IS NULL
                INNER JOIN products p ON oi.product_id = p.id AND p.deleted_at IS NULL
                WHERE o.deleted_at IS NULL 
                    AND o.status = 'COMPLETED'
                    AND DATE(o.created_at) BETWEEN ? AND ?
                GROUP BY p.id, p.name
                HAVING SUM(oi.quantity) > ?
                ORDER BY total_revenue DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(sql, topProductMapper, startDate, endDate, minQuantity, limit);
    }

    @Override
    public List<CustomerAnalyticsDto> getCustomerAnalytics(Long minOrders) {
        String sql = """
                SELECT 
                    u.id AS user_id,
                    u.name AS user_name,
                    COUNT(o.id) AS total_orders,
                    SUM(o.total_amount) AS lifetime_value,
                    AVG(o.total_amount) AS avg_order_value,
                    MAX(o.created_at) AS last_order_date,
                    MIN(o.created_at) AS first_order_date,
                    DATEDIFF( MIN(o.created_at), MAX(o.created_at)) AS customer_lifetime_days
                FROM users u
                LEFT JOIN orders o ON u.id = o.user_id AND o.deleted_at IS NULL
                WHERE u.deleted_at IS NULL
                GROUP BY u.id, u.name
                HAVING COUNT(o.id) >= ?
                ORDER BY lifetime_value DESC
                """;
        return jdbcTemplate.query(sql, customerAnalyticsMapper, minOrders);
    }

    @Override
    public List<OrderDetailWithProductDto> getOrdersWithProductInfo(Long userId) {
        String sql = """
                SELECT 
                    o.id AS order_id,
                    o.user_id,
                    o.total_amount,
                    o.status,
                    o.created_at,
                    COUNT(oi.id) AS item_count,
                    GROUP_CONCAT(CONCAT(p.name, ' x', oi.quantity) SEPARATOR ', ') AS items_summary,
                    CASE 
                        WHEN p.stock_quantity > 10 THEN 'IN_STOCK'
                        WHEN p.stock_quantity > 0 THEN 'LOW_STOCK'
                        ELSE 'OUT_OF_STOCK'
                    END AS inventory_status
                FROM orders o
                LEFT JOIN order_items oi ON o.id = oi.order_id AND oi.deleted_at IS NULL
                LEFT JOIN products p ON oi.product_id = p.id AND p.deleted_at IS NULL
                WHERE o.deleted_at IS NULL
                    AND o.user_id = ?
                GROUP BY o.id, o.user_id, o.total_amount, o.status, o.created_at, p.stock_quantity
                ORDER BY o.created_at DESC
                """;
        return jdbcTemplate.query(sql, orderDetailMapper, userId);
    }

    @Override
    public Long getTotalOrdersByStatusInPeriod(String status, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = """
                SELECT COUNT(o.id) AS total
                FROM orders o
                WHERE o.deleted_at IS NULL 
                    AND o.status = ?
                    AND o.created_at BETWEEN ? AND ?
                """;
        Long result = jdbcTemplate.queryForObject(sql, Long.class, status, startDate, endDate);
        return result != null ? result : 0L;
    }

    @Override
    public Double getAverageOrderValueByStatus(String status) {
        String sql = """
                SELECT AVG(o.total_amount) AS avg_value
                FROM orders o
                WHERE o.deleted_at IS NULL 
                    AND o.status = ?
                """;
        Double result = jdbcTemplate.queryForObject(sql, Double.class, status);
        return result != null ? result : 0.0;
    }
}

