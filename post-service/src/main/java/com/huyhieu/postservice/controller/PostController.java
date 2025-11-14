package com.huyhieu.postservice.controller;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostResponse;
import com.huyhieu.common.enums.Visibility;
import com.huyhieu.postservice.dto.request.CreatePostRequest;
import com.huyhieu.postservice.service.PostService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
  PostService postService;

  @PostMapping("/internal/cold-feed")
  public ApiResponse<PageResponse<PostResponse>> getColdFeed(
      @RequestBody List<String> authorIds,
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size) {

    return ApiResponse.<PageResponse<PostResponse>>builder()
        .result(postService.getColdFeed(authorIds, page, size))
        .build();
  }

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ApiResponse<PostResponse> createPost(
      @RequestParam("content") String content,
      @RequestParam("visibility") Visibility visibility,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    CreatePostRequest request =
        CreatePostRequest.builder().content(content).visibility(visibility).build();

    return ApiResponse.<PostResponse>builder()
        .result(postService.createPost(request, files))
        .build();
  }

  @PostMapping("/internal/batch-details")
  public ApiResponse<List<PostResponse>> getPostsBatchDetails(@RequestBody List<String> postIds) {
    return ApiResponse.<List<PostResponse>>builder()
        .result(postService.getPostsBatch(postIds))
        .build();
  }

  @GetMapping("/me")
  public ApiResponse<PageResponse<PostResponse>> getMyPosts(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
    return ApiResponse.<PageResponse<PostResponse>>builder()
        .result(postService.getMyPosts(page, size))
        .build();
  }

  @GetMapping("/internal/{postId}/author")
  public ResponseEntity<String> getPostAuthorId(@PathVariable String postId) {
    return ResponseEntity.ok(postService.getPostAuthorId(postId));
  }
}
