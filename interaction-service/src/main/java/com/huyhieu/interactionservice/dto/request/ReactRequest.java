package com.huyhieu.interactionservice.dto.request;

import com.huyhieu.common.enums.ReactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReactRequest {
  ReactionType reactionType;
}
