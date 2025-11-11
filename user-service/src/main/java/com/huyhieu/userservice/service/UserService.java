package com.huyhieu.userservice.service;

import com.huyhieu.userservice.dto.response.UserRepresentationDto;
import com.huyhieu.userservice.dto.response.UserResponse;

public interface UserService {
  UserResponse getMyInfo(String userId);

  void createUserByKeycloakEvent(String userId, UserRepresentationDto userDto);

  void updateUserByKeycloakEvent(String userId, UserRepresentationDto userDto);

  void deleteUser(String userId);
}
