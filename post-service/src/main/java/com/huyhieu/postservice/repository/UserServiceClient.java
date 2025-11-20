package com.huyhieu.postservice.repository;

import com.huyhieu.common.dto.request.UserIdsRequest;
import com.huyhieu.common.dto.response.ApiResponse;

import java.util.List;
import java.util.Map;

import com.huyhieu.common.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${app.services.user-service-url}")
public interface UserServiceClient {
  @PostMapping("/api/v1/users/internal/users/batch-info")
  ApiResponse<Map<String, UserResponse>> getBatchUserInfos(@RequestBody UserIdsRequest request);
}
