package org.example.miniecommerce.service;

import org.example.miniecommerce.dto.auth.AuthResponse;
import org.example.miniecommerce.dto.auth.LoginRequest;
import org.example.miniecommerce.dto.user.CreateUserRequest;

public interface AuthService {
    AuthResponse register(CreateUserRequest request);
    AuthResponse login(LoginRequest request);
}
