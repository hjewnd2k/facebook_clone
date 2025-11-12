package com.huyhieu.postservice.dto.response;

import java.util.List;

import com.huyhieu.postservice.entity.Media;
import com.huyhieu.postservice.enums.Visibility;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
  String id;
  String userId;
  String content;
  List<Media> media;
  Visibility visibility;
}
