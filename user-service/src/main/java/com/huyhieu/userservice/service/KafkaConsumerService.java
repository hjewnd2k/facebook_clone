package com.huyhieu.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huyhieu.userservice.dto.response.AdminEventDto;
import com.huyhieu.userservice.dto.response.KeycloakEventDto;
import com.huyhieu.userservice.dto.response.UserRepresentationDto;
import com.huyhieu.userservice.entity.User;
import com.huyhieu.userservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaConsumerService {
  UserRepository userRepository;
  ObjectMapper objectMapper;

  // === THÊM LISTENER DEBUG NÀY VÀO ===
  @KafkaListener(topics = "keycloak_admin_events")
  public void handleRawString(String message) {
    log.warn("DEBUG LISTENER: ĐÃ NHẬN ĐƯỢC STRING: {}", message);
  }

  @KafkaListener(topics = "keycloak_user_events")
  public void handleAdminEvent(AdminEventDto event) { // 2. SỬA DTO Ở ĐÂY
    log.info(
        "Nhận được Admin Event: {} cho resource: {}",
        event.getOperationType(),
        event.getResourceType());

    // Chúng ta chỉ quan tâm đến sự kiện liên quan đến USER
    if (!"USER".equals(event.getResourceType())) {
      return;
    }

    // Lấy ID user từ resourcePath (ví dụ: "users/0cbc6f0a-...")
    String userId = event.getResourceId();
    // Hoặc parse từ resourcePath nếu resourceId không có
    if (userId == null
        && event.getResourcePath() != null
        && event.getResourcePath().startsWith("users/")) {
      userId = event.getResourcePath().substring(6);
    }

    if (userId == null) {
      log.warn("Không tìm thấy userId trong Admin Event");
      return;
    }

    switch (event.getOperationType()) {
      case CREATE:
        try {
          // 3. Parse JSON lồng nhau
          UserRepresentationDto userDto =
              objectMapper.readValue(event.getRepresentation(), UserRepresentationDto.class);
          log.info("Xử lý CREATE user: {}", userDto.getUsername());

          User newUser = new User(userId, userDto.getUsername());
          newUser.setDisplayName(userDto.getFirstName() + " " + userDto.getLastName());
          userRepository.save(newUser);

        } catch (JsonProcessingException e) {
          log.error("Lỗi parse 'representation' khi CREATE user", e);
        }
        break;

      case UPDATE:
        try {
          UserRepresentationDto userDto =
              objectMapper.readValue(event.getRepresentation(), UserRepresentationDto.class);
          log.info("Xử lý UPDATE user: {}", userDto.getUsername());

          userRepository
              .findByUserId(userId)
              .ifPresent(
                  user -> {
                    user.setUsername(userDto.getUsername());
                    user.setDisplayName(userDto.getFirstName() + " " + userDto.getLastName());
                    userRepository.save(user);
                  });

        } catch (JsonProcessingException e) {
          log.error("Lỗi parse 'representation' khi UPDATE user", e);
        }
        break;

      case DELETE:
        log.info("Xử lý DELETE user: {}", userId);
        userRepository.deleteByUserId(userId);
        break;

      default:
        log.warn("OperationType không được xử lý: {}", event.getOperationType());
        break;
    }
  }
}
