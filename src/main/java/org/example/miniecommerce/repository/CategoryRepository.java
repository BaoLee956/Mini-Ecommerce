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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public Page<Category> findAll(Pageable pageable) {
        String sql = "SELECT * FROM categories ORDER BY id DESC LIMIT :limit OFFSET :offset";
        Map<String, Object> params = Map.of(
                "limit", pageable.getPageSize(),
                "offset", pageable.getPageNumber() * pageable.getPageSize());

        List<Category> content = jdbc.query(sql, params, new BeanPropertyRowMapper<>(Category.class));
        long total = jdbc.getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM categories", Long.class);

        return new PageImpl<>(content, pageable, total);
    }

    public Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable) {
        String sql = "SELECT * FROM categories WHERE LOWER(name) LIKE :keyword ORDER BY id DESC LIMIT :limit OFFSET :offset";
        String likeKeyword = "%" + keyword.toLowerCase() + "%";
        Map<String, Object> params = Map.of(
                "keyword", likeKeyword,
                "limit", pageable.getPageSize(),
                "offset", pageable.getPageNumber() * pageable.getPageSize());

        List<Category> content = jdbc.query(sql, params, new BeanPropertyRowMapper<>(Category.class));
        long total = jdbc.queryForObject("SELECT COUNT(*) FROM categories WHERE LOWER(name) LIKE :keyword",
                Map.of("keyword", likeKeyword), Long.class);

        return new PageImpl<>(content, pageable, total);
    }

    public Optional<Category> findById(Long id) {
        String sql = "SELECT * FROM categories WHERE id = :id";
        try {
            Category category = jdbc.queryForObject(sql, Map.of("id", id), new BeanPropertyRowMapper<>(Category.class));
            return Optional.of(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Category insert(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (:name, :description)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", category.getName())
                .addValue("description", category.getDescription());

        jdbc.update(sql, params, keyHolder);

        category.setId(keyHolder.getKey().longValue());
        return category;
    }

    public Category update(Category category) {
        String sql = "UPDATE categories SET name = :name, description = :description WHERE id = :id";

        jdbc.update(sql, Map.of(
                "name", category.getName(),
                "description", category.getDescription(),
                "id", category.getId()));
        return category;
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM categories WHERE id = :id", Map.of("id", id));
    }
}