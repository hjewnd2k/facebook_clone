package com.huyhieu.interactionservice.service;

import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostStatsDTO;
import com.huyhieu.interactionservice.dto.request.CommentRequest;
import com.huyhieu.interactionservice.dto.response.CommentResponse;

import java.util.List;
import java.util.Map;

public interface CommentService {
  CommentResponse createComment(String postId, CommentRequest request);

  PageResponse<CommentResponse> getCommentsForPost(String postId, int page, int size);

  Map<String, PostStatsDTO> getStatsForPosts(List<String> postIds);
}
