package com.huyhieu.notificationservice.config;

import com.huyhieu.common.constants.WebSocketDestinations;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Kích hoạt STOMP
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final JwtAuthChannelInterceptor jwtAuthChannelInterceptor;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // "Broker" là nơi server gửi message ĐẾN client
    // FE sẽ lắng nghe các topic bắt đầu bằng /user
    config.enableSimpleBroker(WebSocketDestinations.USER_PREFIX);
    // "AppDestination" là nơi client gửi message ĐẾN server
    config.setApplicationDestinationPrefixes(WebSocketDestinations.APP_PREFIX);
    // Cho phép gửi thông báo cá nhân (ví dụ: /user/{userId}/queue/notifications)
    config.setUserDestinationPrefix(WebSocketDestinations.USER_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Đây là endpoint mà client sẽ kết nối (giống API)pNotificationConsumer
    // Gateway sẽ điều hướng /ws đến đây
    registry
        .addEndpoint("/ws")
        .setAllowedOrigins("http://localhost:8080") // (Cho Swagger Gateway)
        // .setAllowedOrigins("*"); // (Tạm thời cho FE local)
        .withSockJS(); // (Dùng SockJS để fallback)
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(jwtAuthChannelInterceptor);
  }
}
