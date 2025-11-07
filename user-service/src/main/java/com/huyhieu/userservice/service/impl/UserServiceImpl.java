package com.huyhieu.userservice.service.impl;

import com.huyhieu.userservice.dto.request.UserRequest;
import com.huyhieu.userservice.dto.response.UserResponse;
import com.huyhieu.userservice.entity.User;
import com.huyhieu.userservice.mapper.UserMapper;
import com.huyhieu.userservice.repository.UserRepository;
import com.huyhieu.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
  UserRepository userRepository;
  UserMapper userMapper;

  @Override
  public UserResponse getMyInfo(String userId) {
    User userProfile =
        userRepository
            .findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return userMapper.toUserResponse(userProfile);
  }
}
