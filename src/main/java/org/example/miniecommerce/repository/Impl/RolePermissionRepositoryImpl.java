package org.example.miniecommerce.repository.Impl;

import org.example.miniecommerce.repository.RolePermisionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RolePermissionRepositoryImpl implements RolePermisionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void assignPermissionToRole(String permissionName, String roleName) {
        String sql = "INSERT INTO role_permissions (role_id, permission_id) " +
                "SELECT r.id, p.id " +
                "FROM roles r JOIN permissions p ON p.name = ? " +
                "WHERE r.name = ?";

        jdbcTemplate.update(sql, permissionName, roleName);
    }

    @Override
    public void assignAllPermissionsToRole(String roleName) {
        String sql = "INSERT INTO role_permissions (role_id, permission_id) " +
                "SELECT r.id, p.id " +
                "FROM roles r CROSS JOIN permissions p " +
                "WHERE r.name = ? " +
                "AND NOT EXISTS ( " +
                "   SELECT 1 FROM role_permissions rp " +
                "   WHERE rp.role_id = r.id AND rp.permission_id = p.id" +
                ")";

        jdbcTemplate.update(sql, roleName);
    }

}
