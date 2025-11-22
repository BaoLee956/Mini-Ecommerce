package org.example.miniecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.miniecommerce.dto.role.CreateRoleRequest;
import org.example.miniecommerce.dto.role.RoleResponse;
import org.example.miniecommerce.dto.role.UpdateRoleRequest;
import org.example.miniecommerce.entity.Role;
import org.example.miniecommerce.factory.RoleFactory;
import org.example.miniecommerce.repository.RoleRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleFactory roleFactory;

    public RoleResponse createRole(CreateRoleRequest request) {
        Role role = roleFactory.createRole(request);
        roleRepository.save(role);
        return roleFactory.toRoleResponse(role);
    }

    public RoleResponse updateRole(Long roleId, UpdateRoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roleFactory.updateRole(role, request);
        roleRepository.save(role);
        return roleFactory.toRoleResponse(role);
    }

    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleFactory::toRoleResponse)
                .collect(Collectors.toList());
    }

    public RoleResponse getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return roleFactory.toRoleResponse(role);
    }
}
