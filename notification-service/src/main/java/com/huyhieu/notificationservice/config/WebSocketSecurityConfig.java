package com.huyhieu.notificationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  /** Tắt CSRF (giống như API REST) */
  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }

  /** Đây là nơi chúng ta định nghĩa "luật" bảo mật */
  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages
        // Tất cả các message (CONNECT, SUBSCRIBE, MESSAGE)
        // đều phải được xác thực (authenticated)
        .anyMessage()
        .authenticated();
  }
}
