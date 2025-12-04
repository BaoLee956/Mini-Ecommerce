package org.example.miniecommerce.repository.Impl;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.entity.OrderItem;
import org.example.miniecommerce.repository.OrderItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<OrderItem> orderItemRowMapper = new RowMapper<>() {
        @Override
        public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(rs.getLong("order_id"));
            orderItem.setProductId(rs.getLong("product_id"));
            orderItem.setQuantity(rs.getInt("quantity"));
            orderItem.setPrice(rs.getBigDecimal("unit_price"));
            orderItem.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            orderItem.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            java.sql.Timestamp deletedAtTimestamp = rs.getTimestamp("deleted_at");
            if (deletedAtTimestamp != null) {
                orderItem.setDeletedAt(deletedAtTimestamp.toLocalDateTime());
            }

            return orderItem;
        }
    };

    @Override
    public Optional<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ? AND product_id = ? AND deleted_at IS NULL";
        List<OrderItem> result = jdbcTemplate.query(sql, orderItemRowMapper, orderId, productId);
        return result.stream().findFirst();
    }

    @Override
    public List<OrderItem> findAll() {
        String sql = "SELECT * FROM order_items WHERE deleted_at IS NULL";
        return jdbcTemplate.query(sql, orderItemRowMapper);
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ? AND deleted_at IS NULL";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }

    @Override
    public void save(OrderItem orderItem) {
        LocalDateTime now = LocalDateTime.now();

        // Check if order item already exists
        Optional<OrderItem> existing = findByOrderIdAndProductId(orderItem.getOrderId(), orderItem.getProductId());

        if (existing.isEmpty()) {
            // Insert new
            String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                now,
                now);
            orderItem.setCreatedAt(now);
            orderItem.setUpdatedAt(now);
        } else {
            // Update existing
            String sql = "UPDATE order_items SET quantity = ?, unit_price = ?, updated_at = ? WHERE order_id = ? AND product_id = ?";
            jdbcTemplate.update(sql,
                orderItem.getQuantity(),
                orderItem.getPrice(),
                now,
                orderItem.getOrderId(),
                orderItem.getProductId());
            orderItem.setUpdatedAt(now);
        }
    }

    @Override
    public void deleteByOrderIdAndProductId(Long orderId, Long productId) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "UPDATE order_items SET deleted_at = ? WHERE order_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, now, orderId, productId);
    }
}

