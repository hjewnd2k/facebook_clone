package com.huyhieu.interactionservice.dto.response;

import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
  String id;
  String postId;
  String userId;
  String content;
  String parentCommentId;
  Instant createdDate;
  Instant updatedDate;
}
