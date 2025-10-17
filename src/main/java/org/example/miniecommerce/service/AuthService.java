package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.auth.AuthResponse;
import org.example.miniecommerce.dto.auth.LoginRequest;
import org.example.miniecommerce.dto.user.CreateUserRequest;
import org.example.miniecommerce.dto.user.UserResponse;
import org.example.miniecommerce.entity.User;
import org.example.miniecommerce.mapper.UserMapper;
import org.example.miniecommerce.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;


  //register
  public AuthResponse register(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = userMapper.toUser(request);
        userRepository.save(user);

        UserResponse userResponse = userMapper.toUserResponse(user);

        return AuthResponse.builder()
                .message("Register successful")
                .user(userResponse)
                .build();
    }

    // login
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        UserResponse userResponse = userMapper.toUserResponse(user);

        return AuthResponse.builder()
                .message("Login successful")
                .user(userResponse)
                .build();
    }
}
