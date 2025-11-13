package com.huyhieu.postservice.repository;

import com.huyhieu.common.enums.Visibility;
import com.huyhieu.postservice.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> { // ID là String
  Page<Post> findByUserId(String userId, Pageable pageable);

  Page<Post> findByUserIdInAndVisibility(
      List<String> userIds, Visibility visibility, Pageable pageable);
}
