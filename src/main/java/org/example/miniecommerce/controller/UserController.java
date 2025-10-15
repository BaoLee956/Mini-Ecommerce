package org.example.miniecommerce.controller;

import java.util.List;

import org.example.miniecommerce.dto.user.CreateUserRequest;
import org.example.miniecommerce.dto.user.UpdateUserRequest;
import org.example.miniecommerce.dto.user.UserResponse;
import org.example.miniecommerce.entity.User;
import org.example.miniecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
    

  // @GetMapping("/{userId}")
  // public UserResponse getUser(@PathVariable String userId){
  //   return userService.getUserId(userId);
  // }
  @GetMapping
  public List <User> getAllUser () {
    return userService.getAllUser();
  }


  @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserId(id));
    }

  @PostMapping
  public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
      return ResponseEntity.ok(userService.createUser(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
          @PathVariable String id,
          @RequestBody UpdateUserRequest request) {
      return ResponseEntity.ok(userService.updateUser(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
      userService.deleteUser(id);
      return ResponseEntity.ok("User deleted");
  }
}