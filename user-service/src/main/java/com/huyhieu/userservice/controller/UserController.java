package com.huyhieu.userservice.controller;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.userservice.dto.response.UserResponse;
import com.huyhieu.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
  UserService userService;

  @GetMapping("/me")
  public ApiResponse<UserResponse> getMyInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();
    return ApiResponse.<UserResponse>builder().result(userService.getMyInfo(userId)).build();
  }

  @GetMapping("/public/hello")
  public ApiResponse<String> helloPublic() {
    return ApiResponse.<String>builder()
        .result("Đây là API public, ai cũng xem được (từ User Service)")
        .build();
  }

  @GetMapping("/internal/{userId}/display-name")
  public ResponseEntity<String> getUserDisplayName(@PathVariable String userId) {
    return ResponseEntity.ok(userService.getUserDisplayName(userId));
  }
}
