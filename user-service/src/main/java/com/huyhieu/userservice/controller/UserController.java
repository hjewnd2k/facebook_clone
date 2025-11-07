package com.huyhieu.userservice.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
  UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getMyInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();
    return ResponseEntity.ok(userService.getMyInfo(userId));
  }

  @GetMapping("/public/hello")
  public String helloPublic() {
    return "Đây là API public, ai cũng xem được (từ User Service)";
  }
}
