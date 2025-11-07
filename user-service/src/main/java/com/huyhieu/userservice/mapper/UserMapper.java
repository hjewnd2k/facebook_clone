package com.huyhieu.userservice.mapper;

import com.huyhieu.userservice.dto.request.UserRequest;
import com.huyhieu.userservice.dto.response.UserResponse;
import com.huyhieu.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserRequest request);

  UserResponse toUserResponse(User user);
}
