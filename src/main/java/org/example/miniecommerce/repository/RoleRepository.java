package org.example.miniecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.example.miniecommerce.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository {
  Optional<Role> findById(Long id);
  Optional<Role> findByName(String name);
  List<Role> findAll();
  void save(Role role);
  void deleteById(Long id);
}
