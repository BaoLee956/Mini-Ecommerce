package org.example.miniecommerce.factory;

import org.example.miniecommerce.dto.user.CreateUserRequest;
import org.example.miniecommerce.dto.user.UpdateUserRequest;
import org.example.miniecommerce.dto.user.UserResponse;
import org.example.miniecommerce.entity.User;
import org.example.miniecommerce.repository.UserRoleRepository;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

@Component
public class UserFactory {

  private final UserRoleRepository userRoleRepository;

  public User toUser(CreateUserRequest req) {
    User user = new User();
    user.setName(req.getName());
    user.setEmail(req.getEmail());
    user.setPassword(req.getPassword());
    return user;
  }

  public void updateUser(User user, UpdateUserRequest req) {
    if (req.getName() != null && !req.getName().isBlank()) {
      user.setName(req.getName());
    }
    if (req.getEmail() != null && !req.getEmail().isBlank()) {
      user.setEmail(req.getEmail());
    }
    if (req.getPassword() != null && !req.getPassword().isBlank()) {
      user.setPassword(req.getPassword());
    }
  }

  /**
   * Chuyển đổi User entity sang UserResponse
   */
  

  public UserResponse toUserResponse(User user) {
    String roleName = userRoleRepository.getRoleNameByUserEmail(user.getEmail());
    return UserResponse.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .roleName(roleName)
        .createdAt(user.getCreatedAt())
        .updateAt(user.getUpdatedAt())
        .build();
  }
}
