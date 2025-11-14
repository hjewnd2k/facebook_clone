package com.huyhieu.interactionservice.entity;

import com.huyhieu.common.enums.ReactionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "reactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reaction {
  @Id String id;
  String postId; // Index
  String userId; // Index
  ReactionType reactionType;

  @CreatedDate Instant createdDate;
  @LastModifiedDate Instant updatedDate;
}
