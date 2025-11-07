package com.huyhieu.userservice.service;

import com.huyhieu.userservice.dto.response.UserResponse;

public interface UserService {
  UserResponse getMyInfo(String userId);
}
