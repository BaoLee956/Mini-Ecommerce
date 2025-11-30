package org.example.miniecommerce.service;

import java.util.List;
import org.example.miniecommerce.dto.user.UpdateUserRequest;
import org.example.miniecommerce.dto.user.UserResponse;

public interface UserService {
    UserResponse updateUser(String userId, UpdateUserRequest request);
    void deleteUser(String userId);
    List<UserResponse> getAllUser();
    UserResponse getUserId(String userId);
}
