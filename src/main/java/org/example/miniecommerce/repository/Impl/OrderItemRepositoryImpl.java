package org.example.miniecommerce.repository.Impl;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.entity.OrderItem;
import org.example.miniecommerce.repository.OrderItemRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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
            orderItem.setId(rs.getLong("id"));
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
    public Optional<OrderItem> findById(Long id) {
        String sql = "SELECT * FROM order_items WHERE id = ? AND deleted_at IS NULL";
        List<OrderItem> result = jdbcTemplate.query(sql, orderItemRowMapper, id);
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

        if (orderItem.getId() == null) {
            String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, created_at) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
                ps.setLong(1, orderItem.getOrder() != null ? orderItem.getOrder().getId() : null);
                ps.setLong(2, orderItem.getProductId());
                ps.setInt(3, orderItem.getQuantity());
                ps.setBigDecimal(4, orderItem.getPrice());
                ps.setObject(5, now);
                return ps;
            }, keyHolder);

            Long generatedId = keyHolder.getKey().longValue();
            orderItem.setId(generatedId);
            orderItem.setCreatedAt(now);
        } else {
            String sql = "UPDATE order_items SET product_id = ?, quantity = ?, unit_price = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, orderItem.getProductId(), orderItem.getQuantity(), orderItem.getPrice(), now, orderItem.getId());
            orderItem.setUpdatedAt(now);
        }
    }

    @Override
    public void deleteById(Long id) {
        LocalDateTime now = LocalDateTime.now();
        String sql = "UPDATE order_items SET deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, now, id);
    }
}

