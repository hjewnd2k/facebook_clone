package com.huyhieu.postservice.service;

import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.postservice.dto.request.CreatePostRequest;
import com.huyhieu.postservice.dto.response.PostResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
  PostResponse createPost(CreatePostRequest request, List<MultipartFile> files);

  PageResponse<PostResponse> getMyPosts(int page, int size);
}
