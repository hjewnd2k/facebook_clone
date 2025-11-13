package com.huyhieu.postservice.dto.request;

import com.huyhieu.postservice.entity.Media;
import com.huyhieu.common.enums.Visibility;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePostRequest {
  String userId;
  String content;
  List<Media> media;
  Visibility visibility;
}
