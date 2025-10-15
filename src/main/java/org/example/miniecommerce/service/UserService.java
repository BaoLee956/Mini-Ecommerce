package org.example.miniecommerce.service;

import java.util.List;

import org.example.miniecommerce.dto.user.CreateUserRequest;
import org.example.miniecommerce.dto.user.UpdateUserRequest;
import org.example.miniecommerce.dto.user.UserResponse;
import org.example.miniecommerce.entity.User;
import org.example.miniecommerce.mapper.UserMapper;
import org.example.miniecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserResponse createUser(CreateUserRequest request){
    // bat loi email da ton tai 
    if (userRepository.existsByEmail(request.getEmail())){
      throw new RuntimeException("Email already exists");
    }
    User user = userMapper.toUser(request);
    userRepository.save(user);
    return userMapper.toUserResponse(user);
  }

  public UserResponse updateUser(String userId , UpdateUserRequest request){
    User user = userRepository.findById(Long.parseLong(userId))
    .orElseThrow(()->new RuntimeException("User not found"));

    userMapper.updateUser(user, request);

    userRepository.save(user);
    return userMapper.toUserResponse(user);
  }

  public void deleteUser (String userId){
    userRepository.deleteById(Long.parseLong(userId));
  }


  public List <User> getAllUser (){
    return userRepository.findAll();
  }

  public UserResponse getUserId(String userId){
    User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(()-> new RuntimeException("User not found"));
    return userMapper.toUserResponse(user);
  }

}
