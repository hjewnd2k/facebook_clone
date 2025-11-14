package com.huyhieu.interactionservice.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
  @Id String id;
  String postId; // Index
  String userId; // Index
  String content;

  String parentCommentId; // Index

  @CreatedDate Instant createdDate;

  @LastModifiedDate Instant updatedDate;
}
