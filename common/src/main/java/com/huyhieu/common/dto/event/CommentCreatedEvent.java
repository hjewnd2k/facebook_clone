package com.huyhieu.common.dto.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentCreatedEvent {
  String commenterId; // (Ai bình luận)
  String postId;
  String commentId;
  // (Cũng thiếu postAuthorId)
}
