package com.huyhieu.interactionservice.repository;

import com.huyhieu.interactionservice.entity.Reaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends MongoRepository<Reaction, String> {
  // Tìm reaction của 1 user trên 1 post (để đảm bảo họ chỉ react 1 lần)
  Optional<Reaction> findByPostIdAndUserId(String postId, String userId);

  // Đếm số reaction
  long countByPostId(String postId);
}
