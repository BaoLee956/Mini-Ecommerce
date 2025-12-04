package org.example.miniecommerce.factory;

import org.example.miniecommerce.dto.permission.CreatePermissionRequest;
import org.example.miniecommerce.dto.permission.PermissionResponse;
import org.example.miniecommerce.dto.permission.UpdatePermissionRequest;
import org.example.miniecommerce.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionFactory {

    public Permission createPermission(CreatePermissionRequest request) {
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        return permission;
    }

    public void updatePermission(Permission permission, UpdatePermissionRequest request) {
        if (request.getName() != null) {
            permission.setName(request.getName());
        }
        if (request.getDescription() != null) {
            permission.setDescription(request.getDescription());
        }
    }

    public PermissionResponse toPermissionResponse(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.setId(permission.getId());
        response.setName(permission.getName());
        response.setDescription(permission.getDescription());
        return response;
    }
}
