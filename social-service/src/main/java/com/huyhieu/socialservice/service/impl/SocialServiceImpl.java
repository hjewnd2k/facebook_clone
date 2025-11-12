package com.huyhieu.socialservice.service.impl;

import com.huyhieu.socialservice.entity.UserNode;
import com.huyhieu.socialservice.repository.SocialRepository;
import com.huyhieu.socialservice.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {
  private final SocialRepository socialRepository;

  private void ensureUserExists(String userId) {
    socialRepository.mergeUser(userId);
  }

  @Override
  public void followUser(String currentUserId, String userIdToFollow) {
    // Đảm bảo cả 2 user đều tồn tại trong Neo4j
    ensureUserExists(currentUserId);
    ensureUserExists(userIdToFollow);

    socialRepository.followUser(currentUserId, userIdToFollow);
  }

  @Override
  public void unfollowUser(String followerId, String followingId) {}

  @Override
  public List<UserNode> findFollowing(String userId) {
    return socialRepository.findFollowing(userId);
  }

  @Override
  public List<UserNode> findFollowers(String userId) {
    return socialRepository.findFollowers(userId);
  }

  @Override
  public List<UserNode> findMutualFollows(String userId, String targetUserId) {
    return socialRepository.findMutualFollows(userId, targetUserId);
  }
}
