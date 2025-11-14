package com.huyhieu.interactionservice.repository;

import com.huyhieu.interactionservice.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
  // Lấy comment gốc (không phải reply) cho 1 bài đăng, phân trang
  Page<Comment> findByPostIdAndParentCommentIdIsNullOrderByCreatedDateDesc(
      String postId, Pageable pageable);

  // Đếm số comment
  long countByPostId(String postId);
}
