package com.huyhieu.postservice.controller;

import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.postservice.dto.request.CreatePostRequest;
import com.huyhieu.postservice.dto.response.PostResponse;
import com.huyhieu.postservice.enums.Visibility;
import com.huyhieu.postservice.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
  PostService postService;

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

  @GetMapping("/me")
  public ApiResponse<PageResponse<PostResponse>> getMyPosts(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
    return ApiResponse.<PageResponse<PostResponse>>builder()
        .result(postService.getMyPosts(page, size))
        .build();
  }

  @GetMapping("/public/hello")
  public ApiResponse<String> helloPublic() {
    return ApiResponse.<String>builder().result("Đây là Post Service!").build();
  }
}
