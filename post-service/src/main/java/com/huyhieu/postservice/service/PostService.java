package com.huyhieu.postservice.service;

import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostResponse;
import com.huyhieu.postservice.dto.request.CreatePostRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
  PostResponse createPost(CreatePostRequest request, List<MultipartFile> files);

  PageResponse<PostResponse> getMyPosts(int page, int size);

  List<PostResponse> getPostsBatch(List<String> postIds);

  PageResponse<PostResponse> getColdFeed(List<String> authorIds, int page, int size);

  String getPostAuthorId(String postId);
}
