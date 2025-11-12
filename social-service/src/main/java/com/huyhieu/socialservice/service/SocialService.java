package com.huyhieu.socialservice.service;

import com.huyhieu.socialservice.entity.UserNode;

import java.util.List;

public interface SocialService {
  void followUser(String followerId, String followingId);

  void unfollowUser(String followerId, String followingId);

  List<UserNode> findFollowing(String userId);

  List<UserNode> findFollowers(String userId);

  List<UserNode> findMutualFollows(String userId, String targetUserId);
}
