package com.huyhieu.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class GatewaySecurityConfig {
  private static final String[] PUBLIC_APIS = {
    "/api/v1/*/public/**",
    "/swagger-ui.html",
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/webjars/**",
    "/v3/api-docs/user-service",
    "/v3/api-docs/post-service",
  };

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    http.authorizeExchange(
            exchange ->
                exchange
                    // Áp dụng cùng một quy tắc: Bất kỳ đường dẫn nào chứa "/public/"
                    .pathMatchers(PUBLIC_APIS)
                    .permitAll()
                    .anyExchange()
                    .authenticated() // Tất cả request khác cần xác thực
            )
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Kích hoạt xác thực JWT

    http.csrf(ServerHttpSecurity.CsrfSpec::disable);
    return http.build();
  }
}
