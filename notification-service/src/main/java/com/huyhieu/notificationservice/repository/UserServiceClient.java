package com.huyhieu.notificationservice.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${app.services.user-service-url}")
public interface UserServiceClient {
  @GetMapping("/api/v1/users/internal/{userId}/display-name")
  String getUserDisplayName(@PathVariable("userId") String userId);
}
