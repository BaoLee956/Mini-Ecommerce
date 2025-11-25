package org.example.miniecommerce.repository;

public interface RolePermisionRepository {
    void assignPermissionToRole(String permissionName, String roleName);
    void assignAllPermissionsToRole(String roleName);
}
 