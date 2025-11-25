package org.example.miniecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.example.miniecommerce.entity.Permission;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository {
    Optional<Permission> findById(Long id);
    Optional<Permission> findByName(String name);
    List<Permission> findAll();
    void save(Permission permission);
    void deleteById(Long id);
}
