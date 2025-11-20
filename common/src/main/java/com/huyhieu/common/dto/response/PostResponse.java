package com.huyhieu.common.dto.response;

import com.huyhieu.common.enums.Visibility;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
  String id;
  UserResponse userResponse;
  String content;
  List<MediaResponse> media;
  Visibility visibility;

  long likeCount;
  long commentCount;

  Instant createdDate;
  Instant updatedDate;
}
