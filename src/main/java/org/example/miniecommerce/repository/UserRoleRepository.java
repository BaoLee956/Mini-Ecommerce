package org.example.miniecommerce.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository {
    void assignRoleToUser(String email, String roleName);
    String getRoleNameByUserEmail(String email);
}
