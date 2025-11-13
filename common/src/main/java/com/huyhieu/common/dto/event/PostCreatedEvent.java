package com.huyhieu.common.dto.event;

import com.huyhieu.common.enums.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCreatedEvent {
  private Instant createdDate;
  private Instant updatedDate;
  private String postId;
  private String authorId;
  private Visibility visibility;
}
