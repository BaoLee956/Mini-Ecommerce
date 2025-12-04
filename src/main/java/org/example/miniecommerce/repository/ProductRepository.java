package org.example.miniecommerce.repository;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.entity.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private final NamedParameterJdbcTemplate jdbc;

    // 1. Lấy tất cả sản phẩm
    public Page<Product> findAll(Pageable pageable) {
        String sql = """
                SELECT p.*, p.category_id AS categoryId
                FROM products p
                WHERE p.deleted_at IS NULL
                ORDER BY p.id DESC
                LIMIT :limit OFFSET :offset
                """;

        Map<String, Object> params = Map.of(
                "limit", pageable.getPageSize(),
                "offset", pageable.getPageNumber() * pageable.getPageSize()
        );

        List<Product> content = jdbc.query(sql, params, new BeanPropertyRowMapper<>(Product.class));
        long total = countActive();
        return new PageImpl<>(content, pageable, total);
    }

    // 2. Tìm theo tên
    public Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable) {
        String sql = """
                SELECT p.*, p.category_id AS categoryId
                FROM products p
                WHERE LOWER(p.name) LIKE :keyword
                  AND p.deleted_at IS NULL
                ORDER BY p.id DESC
                LIMIT :limit OFFSET :offset
                """;

        String likeKeyword = "%" + keyword.toLowerCase() + "%";
        Map<String, Object> params = Map.of(
                "keyword", likeKeyword,
                "limit", pageable.getPageSize(),
                "offset", pageable.getPageNumber() * pageable.getPageSize()
        );

        List<Product> content = jdbc.query(sql, params, new BeanPropertyRowMapper<>(Product.class));
        long total = countActiveByKeyword(likeKeyword);
        return new PageImpl<>(content, pageable, total);
    }

    // 3. Tìm theo category_id
    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        String sql = """
                SELECT p.*, p.category_id AS categoryId
                FROM products p
                WHERE p.category_id = :categoryId
                  AND p.deleted_at IS NULL
                ORDER BY p.id DESC
                LIMIT :limit OFFSET :offset
                """;

        Map<String, Object> params = Map.of(
                "categoryId", categoryId,
                "limit", pageable.getPageSize(),
                "offset", pageable.getPageNumber() * pageable.getPageSize()
        );

        List<Product> content = jdbc.query(sql, params, new BeanPropertyRowMapper<>(Product.class));
        long total = countActiveByCategoryId(categoryId);
        return new PageImpl<>(content, pageable, total);
    }

    // 4. Tìm theo ID (chi tiết 1 sản phẩm)
    public Optional<Product> findById(Long id) {
        String sql = """
                SELECT p.*, p.category_id AS categoryId
                FROM products p
                WHERE p.id = :id AND p.deleted_at IS NULL
                """;
        try {
            Product product = jdbc.queryForObject(sql, Map.of("id", id), new BeanPropertyRowMapper<>(Product.class));
            return Optional.of(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // 5. Thêm mới
    public Product insert(Product product) {
        String sql = """
                INSERT INTO products (name, description, price, stock_quantity, category_id, created_at, updated_at)
                VALUES (:name, :description, :price, :stock, :categoryId, :now, :now)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("description", product.getDescription())
                .addValue("price", product.getPrice())
                .addValue("stock", product.getStockQuantity())
                .addValue("categoryId", product.getCategory().getId())  // vẫn dùng object vì lúc này có
                .addValue("now", now);

        jdbc.update(sql, params, keyHolder);
        product.setId(keyHolder.getKey().longValue());
        product.setCategoryId(product.getCategory().getId()); // đồng bộ categoryId
        return product;
    }

    // 6. Cập nhật
    public Product update(Product product) {
        String sql = """
                UPDATE products
                SET name = :name,
                    description = :description,
                    price = :price,
                    stock_quantity = :stock,
                    category_id = :categoryId,
                    updated_at = :now
                WHERE id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("description", product.getDescription())
                .addValue("price", product.getPrice())
                .addValue("stock", product.getStockQuantity())
                .addValue("categoryId", product.getCategory().getId())
                .addValue("id", product.getId())
                .addValue("now", LocalDateTime.now());

        jdbc.update(sql, params);
        product.setCategoryId(product.getCategory().getId()); // đồng bộ lại
        return product;
    }

    // 7. Xóa mềm
    public void delete(Long id) {
        String sql = """
                UPDATE products
                SET deleted_at = :now
                WHERE id = :id AND deleted_at IS NULL
                """;

        int updated = jdbc.update(sql, Map.of("id", id, "now", LocalDateTime.now()));
        if (updated == 0) {
            throw new RuntimeException("Product not found or already deleted");
        }
    }

    // === Count methods ===
    private long countActive() {
        return jdbc.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM products WHERE deleted_at IS NULL", Long.class);
    }

    private long countActiveByKeyword(String keyword) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM products WHERE LOWER(name) LIKE :keyword AND deleted_at IS NULL",
                Map.of("keyword", keyword), Long.class);
    }

    private long countActiveByCategoryId(Long categoryId) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM products WHERE category_id = :categoryId AND deleted_at IS NULL",
                Map.of("categoryId", categoryId), Long.class);
    }
    public boolean existsByNameIgnoreCaseTrim(String name) {
        String sql = """
        SELECT COUNT(*) FROM products 
        WHERE TRIM(LOWER(name)) = TRIM(LOWER(:name))
          AND deleted_at IS NULL
        """;
        Integer count = jdbc.queryForObject(sql, Map.of("name", name), Integer.class);
        return count != null && count > 0;
    }

    public boolean existsByNameIgnoreCaseTrimAndIdNot(String name, Long excludeId) {
        String sql = """
        SELECT COUNT(*) FROM products 
        WHERE TRIM(LOWER(name)) = TRIM(LOWER(:name))
          AND id != :id 
          AND deleted_at IS NULL
        """;
        Integer count = jdbc.queryForObject(sql, Map.of("name", name, "id", excludeId), Integer.class);
        return count != null && count > 0;
    }
}