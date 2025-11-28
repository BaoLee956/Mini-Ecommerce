package org.example.miniecommerce.repository;

import org.example.miniecommerce.entity.Shipping;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ShippingRepository {

    private final JdbcTemplate jdbcTemplate;

    public ShippingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Shipping shipping) {
        String sql = "INSERT INTO shipments (order_id, delivery_address, city, postal_code, country, carrier_name, tracking_number, shipping_cost, status, shipped_at, expected_delivery_date, delivered_at, notes, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        jdbcTemplate.update(sql,
            shipping.getOrderId(),
            shipping.getDeliveryAddress(),
            shipping.getCity(),
            shipping.getPostalCode(),
            shipping.getCountry(),
            shipping.getCarrierName(),
            shipping.getTrackingNumber(),
            shipping.getShippingCost(),
            shipping.getStatus().name(),
            shipping.getShippedAt(),
            shipping.getExpectedDeliveryDate(),
            shipping.getDeliveredAt(),
            shipping.getNotes()
        );
    }

    public void update(Shipping shipping) {
        String sql = "UPDATE shipments SET delivery_address = ?, city = ?, postal_code = ?, country = ?, carrier_name = ?, tracking_number = ?, shipping_cost = ?, status = ?, shipped_at = ?, expected_delivery_date = ?, delivered_at = ?, notes = ?, updated_at = NOW() WHERE id = ?";
        jdbcTemplate.update(sql,
            shipping.getDeliveryAddress(),
            shipping.getCity(),
            shipping.getPostalCode(),
            shipping.getCountry(),
            shipping.getCarrierName(),
            shipping.getTrackingNumber(),
            shipping.getShippingCost(),
            shipping.getStatus().name(),
            shipping.getShippedAt(),
            shipping.getExpectedDeliveryDate(),
            shipping.getDeliveredAt(),
            shipping.getNotes(),
            shipping.getId()
        );
    }

    public Optional<Shipping> findById(Long id) {
        String sql = "SELECT id, order_id, delivery_address, city, postal_code, country, carrier_name, tracking_number, shipping_cost, status, shipped_at, expected_delivery_date, delivered_at, notes FROM shipments WHERE id = ?";
        List<Shipping> results = jdbcTemplate.query(sql, ps -> ps.setLong(1, id), (rs, rowNum) -> {
            Shipping s = new Shipping();
            s.setId(rs.getLong("id"));
            s.setDeliveryAddress(rs.getString("delivery_address"));
            s.setCity(rs.getString("city"));
            s.setPostalCode(rs.getString("postal_code"));
            s.setCountry(rs.getString("country"));
            s.setCarrierName(rs.getString("carrier_name"));
            s.setTrackingNumber(rs.getString("tracking_number"));
            s.setShippingCost(rs.getBigDecimal("shipping_cost"));
            s.setStatus(Shipping.Status.valueOf(rs.getString("status")));
            if (rs.getTimestamp("shipped_at") != null) {
                s.setShippedAt(rs.getTimestamp("shipped_at").toLocalDateTime());
            }
            if (rs.getTimestamp("expected_delivery_date") != null) {
                s.setExpectedDeliveryDate(rs.getTimestamp("expected_delivery_date").toLocalDateTime());
            }
            if (rs.getTimestamp("delivered_at") != null) {
                s.setDeliveredAt(rs.getTimestamp("delivered_at").toLocalDateTime());
            }
            s.setNotes(rs.getString("notes"));
            return s;
        });
        return results.stream().findFirst();
    }

    public List<Shipping> findByOrderId(Long orderId) {
        String sql = "SELECT id, order_id, delivery_address, city, postal_code, country, carrier_name, tracking_number, shipping_cost, status, shipped_at, expected_delivery_date, delivered_at, notes FROM shipments WHERE order_id = ?";
        return jdbcTemplate.query(sql, ps -> ps.setLong(1, orderId), (rs, rowNum) -> {
            Shipping s = new Shipping();
            s.setId(rs.getLong("id"));
            s.setDeliveryAddress(rs.getString("delivery_address"));
            s.setCity(rs.getString("city"));
            s.setPostalCode(rs.getString("postal_code"));
            s.setCountry(rs.getString("country"));
            s.setCarrierName(rs.getString("carrier_name"));
            s.setTrackingNumber(rs.getString("tracking_number"));
            s.setShippingCost(rs.getBigDecimal("shipping_cost"));
            s.setStatus(Shipping.Status.valueOf(rs.getString("status")));
            if (rs.getTimestamp("shipped_at") != null) {
                s.setShippedAt(rs.getTimestamp("shipped_at").toLocalDateTime());
            }
            if (rs.getTimestamp("expected_delivery_date") != null) {
                s.setExpectedDeliveryDate(rs.getTimestamp("expected_delivery_date").toLocalDateTime());
            }
            if (rs.getTimestamp("delivered_at") != null) {
                s.setDeliveredAt(rs.getTimestamp("delivered_at").toLocalDateTime());
            }
            s.setNotes(rs.getString("notes"));
            return s;
        });
    }
}