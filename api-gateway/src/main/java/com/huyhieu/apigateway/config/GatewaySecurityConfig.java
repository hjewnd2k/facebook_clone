package com.huyhieu.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
                    .pathMatchers(HttpMethod.OPTIONS)
                    .permitAll()
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Đặt tên các Origins mà FE có thể gọi đến
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000", // FE của bạn
                "http://localhost:8080"  // Swagger UI
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//  @Bean
//  CorsWebFilter corsWebFilter() {
//    CorsConfiguration corsConfiguration = new CorsConfiguration();
//    corsConfiguration.setAllowedOrigins(List.of("*"));
//    corsConfiguration.setAllowedHeaders(List.of("*"));
//    corsConfiguration.setAllowedMethods(List.of("*"));
//
//    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
//        new UrlBasedCorsConfigurationSource();
//    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//
//    return new CorsWebFilter(urlBasedCorsConfigurationSource);
//  }
}
