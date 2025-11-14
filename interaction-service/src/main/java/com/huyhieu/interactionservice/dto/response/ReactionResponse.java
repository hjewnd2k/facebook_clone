package com.huyhieu.interactionservice.dto.response;

import com.huyhieu.common.enums.ReactionType;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReactionResponse {
  String id;
  String postId;
  String userId;
  ReactionType reactionType;
  Instant createdDate;
  Instant updatedDate;
}
