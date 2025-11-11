package com.huyhieu.common.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class JpaAuditingConfig {

  @Bean
  public AuditorAware<String> auditorProvider() {
    // Trả về một implementation của AuditorAware
    return () -> {
      // Lấy thông tin xác thực từ SecurityContext
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !authentication.isAuthenticated()) {
        // Nếu không có ai đăng nhập (ví dụ: job chạy ngầm)
        return Optional.of("SYSTEM");
      }

      try {
        // Lấy đối tượng Jwt từ token
        Jwt jwt = (Jwt) authentication.getPrincipal();
        // Trả về ID (subject) của user từ Keycloak
        return Optional.of(jwt.getSubject());
      } catch (Exception e) {
        // Xử lý nếu có lỗi (ví dụ: không phải JWT)
        return Optional.of("UNKNOWN");
      }
    };
  }
}
