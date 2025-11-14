package com.huyhieu.postservice.repository;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.common.dto.response.PostStatsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "interaction-service", url = "${app.services.interaction-service-url}")
public interface InteractionServiceClient {
  @PostMapping("/api/v1/interactions/internal/posts/batch-stats")
  ApiResponse<Map<String, PostStatsDTO>> getBatchPostStats(@RequestBody List<String> postIds);
}
