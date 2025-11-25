package org.example.miniecommerce.repository.Impl;

import org.example.miniecommerce.repository.UserRoleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRoleRepositotyImpl implements UserRoleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void assignRoleToUser(String email, String roleName) {
        String sql = "INSERT INTO user_roles (user_id, role_id) " +
                "SELECT u.id, r.id " +
                "FROM users u JOIN roles r ON r.name = ? " +
                "WHERE u.email = ?";

        jdbcTemplate.update(sql, roleName, email);
    }

    @Override
    public String getRoleNameByUserEmail(String email) {
        String sql = "SELECT r.name FROM roles r " +
                "JOIN user_roles ur ON r.id = ur.role_id " +
                "JOIN users u ON u.id = ur.user_id " +
                "WHERE u.email = ?";
        return jdbcTemplate.queryForObject(sql, String.class, email);
    }

}
