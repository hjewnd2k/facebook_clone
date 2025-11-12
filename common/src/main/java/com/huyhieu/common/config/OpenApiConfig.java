package com.huyhieu.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình này sẽ định nghĩa 2 điều: 1. @SecurityScheme: Định nghĩa "JWT Bearer Auth" là một phương
 * thức bảo mật. 2. @OpenAPIDefinition: Áp dụng phương thức bảo mật đó (bearerAuth) cho TẤT CẢ các
 * API trong service này.
 */

// 1. Định nghĩa một "Sơ đồ bảo mật" (Security Scheme)
@SecurityScheme(
    name = "bearerAuth", // Đây là tên chúng ta sẽ tham chiếu
    type = SecuritySchemeType.HTTP, // Loại là HTTP
    scheme = "bearer", // Sơ đồ là "bearer"
    bearerFormat = "JWT" // Định dạng là "JWT"
    )
// 2. Áp dụng sơ đồ đó cho TẤT CẢ các API
@OpenAPIDefinition(
    info = @Info(title = "Microservice API", version = "v1"),
    security = {
      @SecurityRequirement(name = "bearerAuth") // Áp dụng 'bearerAuth'
    })
@Configuration
public class OpenApiConfig {}
