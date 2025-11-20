package com.huyhieu.userservice.service;

import com.huyhieu.common.dto.response.UserResponse;
import com.huyhieu.userservice.dto.response.UserRepresentationDto;

import java.util.List;
import java.util.Map;

public interface UserService {
  UserResponse getMyInfo(String userId);

  UserResponse getByUserId(String userId);

  void createUserByKeycloakEvent(String userId, UserRepresentationDto userDto);

  void updateUserByKeycloakEvent(String userId, UserRepresentationDto userDto);

  void deleteUser(String userId);

  String getUserDisplayName(String userId);

  Map<String, UserResponse> getBatchUserInfos(List<String> userIds);
}
