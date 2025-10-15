package org.example.miniecommerce.mapper;

import org.example.miniecommerce.dto.user.CreateUserRequest;
import org.example.miniecommerce.dto.user.UpdateUserRequest;
import org.example.miniecommerce.dto.user.UserResponse;
import org.example.miniecommerce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(CreateUserRequest request); // map cac request vao trong user

  UserResponse toUserResponse (User user); //map user sang userResponse

  void updateUser(@MappingTarget User user , UpdateUserRequest request);
}
