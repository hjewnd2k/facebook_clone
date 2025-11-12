package com.huyhieu.postservice.entity;

import com.huyhieu.common.entity.BaseEntity;
import com.huyhieu.postservice.enums.Visibility;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data // Kế thừa các trường của BaseEntity
@Document(collection = "posts") // Tên collection trong MongoDB
public class Post {
  @Id private String id;

  @CreatedDate private Instant createdDate;

  @LastModifiedDate private Instant updatedDate;

  private String userId; // ID (String) của user từ Keycloak
  private String content; // Nội dung text
  private List<Media> media; // Danh sách ảnh/video
  private Visibility visibility; // "PUBLIC", "FRIENDS", "PRIVATE"
}
