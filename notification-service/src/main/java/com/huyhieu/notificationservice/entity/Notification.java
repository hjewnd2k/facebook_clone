package com.huyhieu.notificationservice.entity;

import com.huyhieu.common.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
  @Id String id;
  String userId; // (Người nhận)
  String actorId; // (Người gây ra event)
  String actorName; // (Tên người gây ra, ví dụ: "Huy Hiếu")
  NotificationType type; // "REACT_POST", "COMMENT_POST"
  String postId;
  boolean isRead = false;

  @CreatedDate Instant createdDate;
  @LastModifiedDate Instant updatedDate;
}
