package org.example.miniecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.miniecommerce.dto.permission.CreatePermissionRequest;
import org.example.miniecommerce.dto.permission.PermissionResponse;
import org.example.miniecommerce.dto.permission.UpdatePermissionRequest;
import org.example.miniecommerce.entity.Permission;
import org.example.miniecommerce.factory.PermissionFactory;
import org.example.miniecommerce.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionFactory permissionFactory;

    @Override
    public PermissionResponse createPermission(CreatePermissionRequest request) {
        Permission permission = permissionFactory.createPermission(request);
        permissionRepository.save(permission);
        return permissionFactory.toPermissionResponse(permission);
    }

    @Override
    public PermissionResponse updatePermission(Long permissionId, UpdatePermissionRequest request) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        permissionFactory.updatePermission(permission, request);
        permissionRepository.save(permission);
        return permissionFactory.toPermissionResponse(permission);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionRepository.deleteById(permissionId);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionFactory::toPermissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PermissionResponse getPermissionById(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        return permissionFactory.toPermissionResponse(permission);
    }
}
