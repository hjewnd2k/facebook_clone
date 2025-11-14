package com.huyhieu.notificationservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthChannelInterceptor implements ChannelInterceptor {

  private final JwtDecoder jwtDecoder;
  private final JwtAuthenticationConverter jwtAuthenticationConverter;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    // Chỉ kiểm tra khi client gửi lệnh CONNECT
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String authHeader = accessor.getFirstNativeHeader("Authorization");
      log.info("Nhận được header Auth: {}", authHeader);

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        try {
          // 1. Giải mã và xác thực Token
          Jwt jwt = jwtDecoder.decode(token);

          // 2. Chuyển đổi thành Authentication
          AbstractAuthenticationToken authentication = jwtAuthenticationConverter.convert(jwt);

          // 3. Đặt vào Header để Spring Security (ở bước sau) đọc được
          accessor.setUser(authentication);
          log.info("Xác thực WebSocket CONNECT thành công cho user: {}", authentication.getName());
        } catch (Exception e) {
          log.error("Xác thực WebSocket thất bại: {}", e.getMessage());
          // (Không set user -> Spring Security sẽ từ chối)
        }
      }
    }
    return message;
  }
}
