package org.example.miniecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.example.miniecommerce.dto.user.UpdateUserRequest;
import org.example.miniecommerce.dto.user.UserResponse;
import org.example.miniecommerce.entity.User;
import org.example.miniecommerce.factory.UserFactory;
import org.example.miniecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserFactory userFactory;

  

  @Override
  public UserResponse updateUser(String userId , UpdateUserRequest request){
    User user = userRepository.findById(Long.parseLong(userId))
    .orElseThrow(()->new RuntimeException("User not found"));

    userFactory.updateUser(user, request);

    userRepository.save(user);
    return userFactory.toUserResponse(user);
  }

  @Override
  public void deleteUser (String userId){
    userRepository.deleteById(Long.parseLong(userId));
  }


  @Override
  public List <UserResponse> getAllUser (){
    List<User> users = userRepository.findAll();

    return users.stream()
                .map(userFactory::toUserResponse)
                .collect(Collectors.toList());
  }

  @Override
  public UserResponse getUserId(String userId){
    User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(()-> new RuntimeException("User not found"));
    return userFactory.toUserResponse(user);
  }

}
