package com.huyhieu.common.dto.event;

import com.huyhieu.common.enums.ReactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostReactedEvent {
  String reactorId; // (Ai react)
  String postId;
  ReactionType reactionType; // (LIKE, LOVE...)

  // Vấn đề: Chúng ta không biết ai là chủ bài viết (postAuthorId)
  // Chúng ta sẽ giải quyết việc này sau khi xây dựng Notification Service
}
