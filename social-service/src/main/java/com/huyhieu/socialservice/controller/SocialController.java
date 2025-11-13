package com.huyhieu.socialservice.controller;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.common.utils.CommonUtils;
import com.huyhieu.socialservice.entity.UserNode;
import com.huyhieu.socialservice.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/social")
@RequiredArgsConstructor
public class SocialController {
  private final SocialService socialService;

  @GetMapping("/internal/followers/{userId}")
  public ApiResponse<List<UserNode>> getFollowersForService(@PathVariable String userId) {
    return ApiResponse.<List<UserNode>>builder()
        .result(socialService.findFollowers(userId))
        .build();
  }

  @GetMapping("/internal/following/{userId}")
  public ApiResponse<List<UserNode>> getFollowingForService(@PathVariable String userId) {
    return ApiResponse.<List<UserNode>>builder()
        .result(socialService.findFollowing(userId))
        .build();
  }

  @PostMapping("/follow/{userIdToFollow}")
  public ApiResponse<?> followUser(@PathVariable String userIdToFollow) {
    String currentUserId = CommonUtils.getCurrentUserId();
    socialService.followUser(currentUserId, userIdToFollow);

    return ApiResponse.builder().message("Follow user successfully").build();
  }

  @PostMapping("/unfollow/{userIdToFollow}")
  public ApiResponse<?> unfollowUser(@PathVariable String userIdToFollow) {
    String currentUserId = CommonUtils.getCurrentUserId();
    socialService.unfollowUser(currentUserId, userIdToFollow);

    return ApiResponse.builder().message("Un follow user successfully").build();
  }

  @GetMapping("/following")
  public ApiResponse<List<UserNode>> getMyFollowing() {
    String userId = CommonUtils.getCurrentUserId();
    return ApiResponse.<List<UserNode>>builder()
        .result(socialService.findFollowing(userId))
        .build();
  }

  @GetMapping("/followers")
  public ApiResponse<List<UserNode>> getMyFollowers() {
    String userId = CommonUtils.getCurrentUserId();
    return ApiResponse.<List<UserNode>>builder()
        .result(socialService.findFollowers(userId))
        .build();
  }

  @GetMapping("/mutual/{otherUserId}")
  public ApiResponse<List<UserNode>> getMutuals(@PathVariable String otherUserId) {
    String userId = CommonUtils.getCurrentUserId();

    return ApiResponse.<List<UserNode>>builder()
        .result(socialService.findMutualFollows(userId, otherUserId))
        .build();
  }
}
