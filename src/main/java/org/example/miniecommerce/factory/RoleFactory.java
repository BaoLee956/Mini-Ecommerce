package org.example.miniecommerce.factory;

import org.example.miniecommerce.dto.role.CreateRoleRequest;
import org.example.miniecommerce.dto.role.RoleResponse;
import org.example.miniecommerce.dto.role.UpdateRoleRequest;
import org.example.miniecommerce.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {

    public Role createRole(CreateRoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        return role;
    }

    public void updateRole(Role role, UpdateRoleRequest request) {
        if (request.getName() != null) {
            role.setName(request.getName());
        }
        if (request.getDescription() != null) {
            role.setName(request.getName());
        }
    }

    public RoleResponse toRoleResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        return response;
    }
}
