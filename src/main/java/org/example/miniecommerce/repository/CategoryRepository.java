package org.example.miniecommerce.repository;

import lombok.RequiredArgsConstructor;
import org.example.miniecommerce.entity.Category;
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
public class CategoryRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public Page<Category> findAll(Pageable pageable) {
        String sql = """
                SELECT * FROM categories
                WHERE deleted_at IS NULL
                ORDER BY id DESC
                LIMIT :limit OFFSET :offset
                """;

        Map<String, Object> params = Map.of(
                "limit", pageable.getPageSize(),
                "offset", pageable.getPageNumber() * pageable.getPageSize());

        List<Category> content = jdbc.query(sql, params, new BeanPropertyRowMapper<>(Category.class));
        long total = countActive();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable) {
        String sql = """
                SELECT * FROM categories
                WHERE LOWER(name) LIKE :keyword
                  AND deleted_at IS NULL
                ORDER BY id DESC
                LIMIT :limit OFFSET :offset
                """;

        String likeKeyword = "%" + keyword.toLowerCase() + "%";
        Map<String, Object> params = Map.of(
                "keyword", likeKeyword,
                "limit", pageable.getPageSize(),
                "offset", pageable.getPageNumber() * pageable.getPageSize());

        List<Category> content = jdbc.query(sql, params, new BeanPropertyRowMapper<>(Category.class));
        long total = countActiveByKeyword(likeKeyword);

        return new PageImpl<>(content, pageable, total);
    }

    public Optional<Category> findById(Long id) {
        String sql = "SELECT * FROM categories WHERE id = :id AND deleted_at IS NULL";
        try {
            Category category = jdbc.queryForObject(sql, Map.of("id", id), new BeanPropertyRowMapper<>(Category.class));
            return Optional.of(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Category insert(Category category) {
        String sql = """
                INSERT INTO categories (name, description, created_at, updated_at)
                VALUES (:name, :description, :now, :now)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", category.getName())
                .addValue("description", category.getDescription())
                .addValue("now", now);

        jdbc.update(sql, params, keyHolder);
        category.setId(keyHolder.getKey().longValue());
        return category;
    }

    public Category update(Category category) {
        String sql = """
                UPDATE categories
                SET name = :name,
                    description = :description,
                    updated_at = :now
                WHERE id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", category.getName())
                .addValue("description", category.getDescription())
                .addValue("id", category.getId())
                .addValue("now", LocalDateTime.now());

        jdbc.update(sql, params);
        return category;
    }

    public void delete(Long id) {
        LocalDateTime now = LocalDateTime.now();

        // 1. Cascade soft-delete tất cả Product thuộc Category này trước
        String sqlProduct = """
                UPDATE products
                SET deleted_at = :now
                WHERE category_id = :categoryId
                AND deleted_at IS NULL
                """;

        MapSqlParameterSource productParams = new MapSqlParameterSource()
                .addValue("now", now)
                .addValue("categoryId", id);

        jdbc.update(sqlProduct, productParams);

        // 2. Sau đó mới soft-delete Category
        String sqlCategory = """
                UPDATE categories
                SET deleted_at = :now
                WHERE id = :id
                AND deleted_at IS NULL
                """;

        MapSqlParameterSource categoryParams = new MapSqlParameterSource()
                .addValue("now", now)
                .addValue("id", id);

        int updated = jdbc.update(sqlCategory, categoryParams);

        if (updated == 0) {
            throw new RuntimeException("Category not found or already deleted");
        }
    }

    private long countActive() {
        String sql = "SELECT COUNT(*) FROM categories WHERE deleted_at IS NULL";
        return jdbc.getJdbcTemplate().queryForObject(sql, Long.class);
    }

    private long countActiveByKeyword(String keyword) {
        String sql = "SELECT COUNT(*) FROM categories WHERE LOWER(name) LIKE :keyword AND deleted_at IS NULL";
        return jdbc.queryForObject(sql, Map.of("keyword", keyword), Long.class);
    }
}