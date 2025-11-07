package com.huyhieu.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(
            authorize ->
                authorize
                        .requestMatchers("/api/v1/*/public/**")
                    .permitAll() // Cho phép API này
                    .anyRequest()
                    .authenticated() // Tất cả API khác cần xác thực
            )
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Kích hoạt xác thực JWT
    return http.build();
  }
}
