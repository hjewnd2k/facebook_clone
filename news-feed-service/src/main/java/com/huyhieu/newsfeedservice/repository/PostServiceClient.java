package com.huyhieu.newsfeedservice.repository;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
    name = "post-service",
    url = "${app.services.post-service-url}" // Sẽ lấy từ .yml
    )
public interface PostServiceClient {

  @PostMapping("/api/v1/posts/internal/batch-details")
  ApiResponse<List<PostResponse>> getPostsBatch(@RequestBody List<String> postIds);

  @PostMapping("/api/v1/posts/internal/cold-feed")
  ApiResponse<PageResponse<PostResponse>> getColdFeed(
      @RequestBody List<String> authorIds,
      @RequestParam("page") int page,
      @RequestParam("size") int size);
}
