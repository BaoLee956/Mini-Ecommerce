package org.example.miniecommerce.repository;

import org.example.miniecommerce.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    List<User> findAll();

    void save(User user);
    void deleteById(Long id);
}