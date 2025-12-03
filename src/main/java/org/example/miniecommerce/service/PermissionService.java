package org.example.miniecommerce.service;

import java.util.List;
import org.example.miniecommerce.dto.permission.CreatePermissionRequest;
import org.example.miniecommerce.dto.permission.PermissionResponse;
import org.example.miniecommerce.dto.permission.UpdatePermissionRequest;

public interface PermissionService {
    PermissionResponse createPermission(CreatePermissionRequest request);
    PermissionResponse updatePermission(Long permissionId, UpdatePermissionRequest request);
    void deletePermission(Long permissionId);
    List<PermissionResponse> getAllPermissions();
    PermissionResponse getPermissionById(Long permissionId);
}
