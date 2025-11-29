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
        String sql = "INSERT INTO shippings (order_id, address, city, postal_code, country, status, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        jdbcTemplate.update(sql,
            shipping.getOrderId(),
            shipping.getAddress(),
            shipping.getCity(),
            shipping.getPostalCode(),
            shipping.getCountry(),
            shipping.getStatus().name()
        );
    }

    public void update(Shipping shipping) {
        String sql = "UPDATE shippings SET address = ?, city = ?, postal_code = ?, country = ?, status = ?, updated_at = NOW() WHERE id = ?";
        jdbcTemplate.update(sql,
            shipping.getAddress(),
            shipping.getCity(),
            shipping.getPostalCode(),
            shipping.getCountry(),
            shipping.getStatus().name(),
            shipping.getId()
        );
    }

    public Optional<Shipping> findById(Long id) {
        String sql = "SELECT id, order_id, address, city, postal_code, country, status FROM shippings WHERE id = ?";
        List<Shipping> results = jdbcTemplate.query(sql, ps -> ps.setLong(1, id), (rs, rowNum) -> {
            Shipping s = new Shipping();
            s.setId(rs.getLong("id"));
            s.setAddress(rs.getString("address"));
            s.setCity(rs.getString("city"));
            s.setPostalCode(rs.getString("postal_code"));
            s.setCountry(rs.getString("country"));
            s.setStatus(Shipping.Status.valueOf(rs.getString("status")));
            return s;
        });
        return results.stream().findFirst();
    }

    public List<Shipping> findByOrderId(Long orderId) {
        String sql = "SELECT id, order_id, address, city, postal_code, country, status FROM shippings WHERE order_id = ?";
        return jdbcTemplate.query(sql, ps -> ps.setLong(1, orderId), (rs, rowNum) -> {
            Shipping s = new Shipping();
            s.setId(rs.getLong("id"));
            s.setAddress(rs.getString("address"));
            s.setCity(rs.getString("city"));
            s.setPostalCode(rs.getString("postal_code"));
            s.setCountry(rs.getString("country"));
            s.setStatus(Shipping.Status.valueOf(rs.getString("status")));
            return s;
        });
    }
}