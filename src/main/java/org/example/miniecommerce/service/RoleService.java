package org.example.miniecommerce.service;

import java.util.List;
import org.example.miniecommerce.dto.role.CreateRoleRequest;
import org.example.miniecommerce.dto.role.RoleResponse;
import org.example.miniecommerce.dto.role.UpdateRoleRequest;

public interface RoleService {
    RoleResponse createRole(CreateRoleRequest request);
    RoleResponse updateRole(Long roleId, UpdateRoleRequest request);
    void deleteRole(Long roleId);
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(Long roleId);
}
