package com.huyhieu.common.dto.response;

import java.time.Instant;

import com.huyhieu.common.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDTO {
  String id;
  String userId; // (Người nhận)
  String actorId; // (Người gây ra event)
  String actorName; // (Tên người gây ra, ví dụ: "Huy Hiếu")
  NotificationType type; // "REACT_POST", "COMMENT_POST"
  String postId;
  boolean isRead = false;

  Instant createdDate;
  Instant updatedDate;
}
