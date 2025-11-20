package com.huyhieu.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  /**
   * Cấu hình CORS toàn cục cho service này. Nó cho phép các request đến từ Swagger UI (chạy ở
   * 8080).
   */
  //  @Override
  //  public void addCorsMappings(CorsRegistry registry) {
  //    registry
  //        .addMapping("/**") // Áp dụng cho tất cả các API (/**)
  //        .allowedOrigins("*")
  //        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các method
  //        .allowedHeaders("*") // Cho phép tất cả header
  //        .allowCredentials(true); // Cho phép gửi cookie/auth
  //  }
}
