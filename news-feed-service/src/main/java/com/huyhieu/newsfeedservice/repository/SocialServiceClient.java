package com.huyhieu.newsfeedservice.repository;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.newsfeedservice.dto.UserNodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "social-service", url = "${app.services.social-service-url}")
public interface SocialServiceClient {

  @GetMapping("/api/v1/social/internal/followers/{userId}")
  ApiResponse<List<UserNodeDTO>> getFollowersForService(@PathVariable("userId") String userId);

  @GetMapping("/api/v1/social/internal/following/{userId}")
  ApiResponse<List<UserNodeDTO>> getFollowingForService(@PathVariable("userId") String userId);
}
