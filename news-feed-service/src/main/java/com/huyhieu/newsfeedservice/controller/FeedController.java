package com.huyhieu.newsfeedservice.controller;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostResponse;
import com.huyhieu.common.utils.CommonUtils;
import com.huyhieu.newsfeedservice.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
@Slf4j
public class FeedController {

  private final FeedService feedService;

  @GetMapping
  public ApiResponse<PageResponse<PostResponse>> getMyFeed(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "20") int size) {
    return ApiResponse.<PageResponse<PostResponse>>builder()
        .result(feedService.getMyFeed(page, size))
        .build();
  }
}
