package org.example.miniecommerce.repository.Impl;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.example.miniecommerce.entity.Permission;
import org.example.miniecommerce.repository.PermissionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryImpl implements PermissionRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Permission> permissionMapper = (rs, row) -> {
        Permission permisson = new Permission();
        permisson.setId(rs.getLong("id"));
        permisson.setName(rs.getString("name"));
        permisson.setDescription(rs.getString("description"));
        return permisson;
    };



    @Override
    public Optional<Permission> findByName(String name) {
        String sql = "SELECT * FROM permissions WHERE name = ?";
        List<Permission> result = jdbcTemplate.query(sql, permissionMapper, name);
        return result.stream().findFirst();
    }

    @Override
    public List<Permission> findAll() {
        String sql = "SELECT * FROM permissions";
        return jdbcTemplate.query(sql, permissionMapper);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        String sql = "SELECT * FROM permissions WHERE id = ?";
        List<Permission> result = jdbcTemplate.query(sql, permissionMapper, id);
        return result.stream().findFirst();
    }

    @Override
    public void save(Permission permission) {
        if (permission.getId() == null) {
            // INSERT
            String sql = "INSERT INTO permissions (name, description) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
                ps.setString(1, permission.getName());
                ps.setString(2, permission.getDescription());
                return ps;
            }, keyHolder);
            permission.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE
            String sql = "UPDATE permissions SET name = ?, description = ? WHERE id = ?";
            jdbcTemplate.update(sql, permission.getName(), permission.getDescription(), permission.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM permissions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
}
