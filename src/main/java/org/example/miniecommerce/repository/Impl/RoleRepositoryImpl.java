package org.example.miniecommerce.repository.Impl;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.example.miniecommerce.entity.Role;
import org.example.miniecommerce.repository.RoleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Role> roleMapper = (rs, row) -> {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        role.setDescription(rs.getString("description"));
        return role;
    };

    @Override
    public Optional<Role> findById(Long id) {
        String sql = "SELECT * FROM roles WHERE id = ?";
        List<Role> result = jdbcTemplate.query(sql, roleMapper, id);
        return result.stream().findFirst();
    }

    @Override
    public Optional<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE name = ?";
        List<Role> result = jdbcTemplate.query(sql, roleMapper, name);
        return result.stream().findFirst();
    }

    @Override
    public List<Role> findAll() {
        String sql = "SELECT * FROM roles";
        return jdbcTemplate.query(sql, roleMapper);
    }


     @Override
    public void save(Role role) {
        if (role.getId() == null) {
            // INSERT
            String sql = "INSERT INTO roles (name, description) VALUES (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
                ps.setString(1, role.getName());
                ps.setString(2, role.getDescription());
                return ps;
            }, keyHolder);

            role.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE
            String sql = "UPDATE roles SET name = ?, description = ? WHERE id = ?";
            jdbcTemplate.update(sql, role.getName(), role.getDescription(), role.getId());
        }
    }

     @Override
     public void deleteById(Long id) {
        String sql = "DELETE FROM roles where id = ?";
        jdbcTemplate.update(sql, id);
     }
}
