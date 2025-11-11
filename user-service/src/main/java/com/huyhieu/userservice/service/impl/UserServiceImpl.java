package com.huyhieu.userservice.service.impl;

import com.huyhieu.userservice.dto.response.UserRepresentationDto;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
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

  @Override
  public void createUserByKeycloakEvent(String userId, UserRepresentationDto userDto) {
    log.info("Xử lý CREATE user: {}", userDto.getUsername());
    User newUser =
        User.builder()
            .userId(userId)
            .username(userDto.getUsername())
            .displayName(userDto.getFirstName() + " " + userDto.getLastName())
            .email(userDto.getEmail())
            .build();

    userRepository.save(newUser);
  }

  @Override
  public void updateUserByKeycloakEvent(String userId, UserRepresentationDto userDto) {
    log.info("Xử lý UPDATE user: {}", userDto.getUsername());
    userRepository
        .findByUserId(userId)
        .ifPresent(
            user -> {
              user.setUsername(userDto.getUsername());
              user.setDisplayName(userDto.getFirstName() + " " + userDto.getLastName());
              user.setEmail(userDto.getEmail());
              userRepository.save(user);
            });
  }

  @Override
  public void deleteUser(String userId) {
    log.info("Xử lý DELETE user: {}", userId);
    userRepository.deleteByUserId(userId);
  }
}
