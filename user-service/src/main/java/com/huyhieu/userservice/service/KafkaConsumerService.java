package com.huyhieu.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huyhieu.common.constants.KafkaTopics;
import com.huyhieu.userservice.dto.response.AdminEventDto;
import com.huyhieu.userservice.dto.response.UserRepresentationDto;
import com.huyhieu.userservice.entity.FailedEvent;
import com.huyhieu.userservice.enums.EventStatus;
import com.huyhieu.userservice.repository.FailedEventRepository;
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
  ObjectMapper objectMapper;
  UserService userService;
  FailedEventRepository failedEventRepository;

  @KafkaListener(topics = KafkaTopics.ADMIN_EVENTS)
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

          userService.createUserByKeycloakEvent(userId, userDto);
        } catch (JsonProcessingException e) {
          log.error("Lỗi parse 'representation' khi CREATE user", e);
        }
        break;

      case UPDATE:
        try {
          UserRepresentationDto userDto =
              objectMapper.readValue(event.getRepresentation(), UserRepresentationDto.class);

          userService.updateUserByKeycloakEvent(userId, userDto);

        } catch (JsonProcessingException e) {
          log.error("Lỗi parse 'representation' khi UPDATE user", e);
        }
        break;

      case DELETE:
        userService.deleteUser(userId);
        break;

      default:
        log.warn("OperationType không được xử lý: {}", event.getOperationType());
        break;
    }
  }

  @KafkaListener(
      topics = KafkaTopics.ADMIN_EVENTS_DLT,
      groupId = "user-service-dlt-group",
      containerFactory = "dltListenerContainerFactory")
  public void handleDeadLetterTopic(String message) {
    log.error("!!! LỖI TỪ DLT (Topic: {}) !!! Đang lưu vào CSDL...", message);

    try {
      FailedEvent failedEvent =
          FailedEvent.builder().payload(message).status(EventStatus.PENDING).build();

      failedEventRepository.save(failedEvent);
      log.info("Đã lưu message lỗi vào CSDL với ID: {}", failedEvent.getId());

    } catch (Exception e) {
      // Nếu việc LƯU LỖI cũng bị lỗi (ví dụ: CSDL sập)
      log.error("KHẨN CẤP: Không thể lưu message DLT vào CSDL. Message: {}", message, e);
    }
  }
}
