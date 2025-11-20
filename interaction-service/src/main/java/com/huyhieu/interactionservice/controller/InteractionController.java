package com.huyhieu.interactionservice.controller;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostStatsDTO;
import com.huyhieu.common.enums.ReactionType;
import com.huyhieu.interactionservice.dto.request.CommentRequest;
import com.huyhieu.interactionservice.dto.request.ReactRequest;
import com.huyhieu.interactionservice.dto.response.CommentResponse;
import com.huyhieu.interactionservice.service.CommentService;
import com.huyhieu.interactionservice.service.ReactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/interactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InteractionController {
  ReactionService reactionService;
  CommentService commentService;

  // === API 1: React một bài đăng ===
  @PostMapping("/posts/{postId}/react")
  public ApiResponse<?> reactToPost(
      @PathVariable String postId, @RequestBody ReactRequest request) {

    reactionService.reactToPost(postId, request.getReactionType());
    // TODO: Gửi event KAFKA "POST_REACTED"
    return ApiResponse.builder().message("React thành công").build();
  }

  // === API 2: Bình luận một bài đăng ===
  @PostMapping("/posts/{postId}/comment")
  public ApiResponse<CommentResponse> createComment(
      @PathVariable String postId, @RequestBody CommentRequest newComment) {

    // TODO: Gửi event KAFKA "COMMENT_CREATED"
    return ApiResponse.<CommentResponse>builder()
        .result(commentService.createComment(postId, newComment))
        .build();
  }

  // === API 3: Lấy bình luận của bài đăng (Phân trang) ===
  @GetMapping("/public/posts/{postId}/comments")
  public ApiResponse<PageResponse<CommentResponse>> getComments(
      @PathVariable String postId,
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
    return ApiResponse.<PageResponse<CommentResponse>>builder()
        .result(commentService.getCommentsForPost(postId, page, size))
        .build();
  }

  // === API 4 (NỘI BỘ): Lấy Stats (Số lượng) ===
  @PostMapping("/internal/posts/batch-stats")
  public ApiResponse<Map<String, PostStatsDTO>> getBatchPostStats(
      @RequestBody List<String> postIds) {
    // Service sẽ lặp qua list ID và đếm từ 2 repository
    return ApiResponse.<Map<String, PostStatsDTO>>builder()
        .result(commentService.getStatsForPosts(postIds))
        .build();
  }
}
