package org.example.miniecommerce.repository;

import org.example.miniecommerce.entity.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Payment payment) {
        String sql = "INSERT INTO payments (order_id, amount, payment_method, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, NOW(), NOW())";
        jdbcTemplate.update(sql,
            payment.getOrderId(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getStatus().name()
        );
    }

    public void update(Payment payment) {
        String sql = "UPDATE payments SET amount = ?, payment_method = ?, status = ?, paid_at = ?, updated_at = NOW() WHERE id = ?";
        jdbcTemplate.update(sql,
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getStatus().name(),
            payment.getPaidAt(),
            payment.getId()
        );
    }

    public Optional<Payment> findById(Long id) {
        String sql = "SELECT id, order_id, amount, payment_method, status, paid_at, created_at, updated_at FROM payments WHERE id = ?";
        List<Payment> results = jdbcTemplate.query(sql, ps -> ps.setLong(1, id), (rs, rowNum) -> {
            Payment p = new Payment();
            p.setId(rs.getLong("id"));
            p.setOrderId(rs.getLong("order_id"));
            p.setAmount(rs.getBigDecimal("amount"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setStatus(Payment.Status.valueOf(rs.getString("status")));
            if (rs.getTimestamp("paid_at") != null) {
                p.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());
            }
            if (rs.getTimestamp("created_at") != null) {
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            if (rs.getTimestamp("updated_at") != null) {
                p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            }
            return p;
        });
        return results.stream().findFirst();
    }

    public List<Payment> findByOrderId(Long orderId) {
        String sql = "SELECT id, order_id, amount, payment_method, status, paid_at, created_at, updated_at FROM payments WHERE order_id = ?";
        return jdbcTemplate.query(sql, ps -> ps.setLong(1, orderId), (rs, rowNum) -> {
            Payment p = new Payment();
            p.setId(rs.getLong("id"));
            p.setOrderId(rs.getLong("order_id"));
            p.setAmount(rs.getBigDecimal("amount"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setStatus(Payment.Status.valueOf(rs.getString("status")));
            if (rs.getTimestamp("paid_at") != null) {
                p.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());
            }
            if (rs.getTimestamp("created_at") != null) {
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            if (rs.getTimestamp("updated_at") != null) {
                p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            }
            return p;
        });
    }

    public List<Payment> findAll() {
        String sql = "SELECT id, order_id, amount, payment_method, status, paid_at, created_at, updated_at FROM payments WHERE deleted_at IS NULL ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Payment p = new Payment();
            p.setId(rs.getLong("id"));
            p.setOrderId(rs.getLong("order_id"));
            p.setAmount(rs.getBigDecimal("amount"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setStatus(Payment.Status.valueOf(rs.getString("status")));
            if (rs.getTimestamp("paid_at") != null) {
                p.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());
            }
            if (rs.getTimestamp("created_at") != null) {
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            if (rs.getTimestamp("updated_at") != null) {
                p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            }
            return p;
        });
    }

    public List<Payment> findByStatus(Payment.Status status) {
        String sql = "SELECT id, order_id, amount, payment_method, status, paid_at, created_at, updated_at FROM payments WHERE status = ? AND deleted_at IS NULL ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, ps -> ps.setString(1, status.name()), (rs, rowNum) -> {
            Payment p = new Payment();
            p.setId(rs.getLong("id"));
            p.setOrderId(rs.getLong("order_id"));
            p.setAmount(rs.getBigDecimal("amount"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setStatus(Payment.Status.valueOf(rs.getString("status")));
            if (rs.getTimestamp("paid_at") != null) {
                p.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());
            }
            if (rs.getTimestamp("created_at") != null) {
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            if (rs.getTimestamp("updated_at") != null) {
                p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            }
            return p;
        });
    }

    public void deleteById(Long id) {
        String sql = "UPDATE payments SET deleted_at = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM payments WHERE deleted_at IS NULL";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public int countByStatus(Payment.Status status) {
        String sql = "SELECT COUNT(*) FROM payments WHERE status = ? AND deleted_at IS NULL";
        return jdbcTemplate.queryForObject(sql, Integer.class, status.name());
    }

    public List<Payment> findAllPaginated(int page, int size) {
        int offset = page * size;
        String sql = "SELECT id, order_id, amount, payment_method, status, paid_at, created_at, updated_at FROM payments WHERE deleted_at IS NULL ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, ps -> {
            ps.setInt(1, size);
            ps.setInt(2, offset);
        }, (rs, rowNum) -> {
            Payment p = new Payment();
            p.setId(rs.getLong("id"));
            p.setOrderId(rs.getLong("order_id"));
            p.setAmount(rs.getBigDecimal("amount"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setStatus(Payment.Status.valueOf(rs.getString("status")));
            if (rs.getTimestamp("paid_at") != null) {
                p.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());
            }
            if (rs.getTimestamp("created_at") != null) {
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            if (rs.getTimestamp("updated_at") != null) {
                p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            }
            return p;
        });
    }

    public List<Payment> findByPaymentMethod(String paymentMethod) {
        String sql = "SELECT id, order_id, amount, payment_method, status, paid_at, created_at, updated_at FROM payments WHERE payment_method = ? AND deleted_at IS NULL ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, ps -> ps.setString(1, paymentMethod), (rs, rowNum) -> {
            Payment p = new Payment();
            p.setId(rs.getLong("id"));
            p.setOrderId(rs.getLong("order_id"));
            p.setAmount(rs.getBigDecimal("amount"));
            p.setPaymentMethod(rs.getString("payment_method"));
            p.setStatus(Payment.Status.valueOf(rs.getString("status")));
            if (rs.getTimestamp("paid_at") != null) {
                p.setPaidAt(rs.getTimestamp("paid_at").toLocalDateTime());
            }
            if (rs.getTimestamp("created_at") != null) {
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            if (rs.getTimestamp("updated_at") != null) {
                p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            }
            return p;
        });
    }
}