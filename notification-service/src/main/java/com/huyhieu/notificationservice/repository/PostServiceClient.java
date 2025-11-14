package com.huyhieu.notificationservice.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service", url = "${app.services.post-service-url}")
public interface PostServiceClient {
  @GetMapping("/api/v1/posts/internal/{postId}/author")
  String getPostAuthorId(@PathVariable("postId") String postId);
}
