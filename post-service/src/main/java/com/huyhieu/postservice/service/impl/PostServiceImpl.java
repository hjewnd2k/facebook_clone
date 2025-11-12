package com.huyhieu.postservice.service.impl;

import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.utils.CommonUtils;
import com.huyhieu.postservice.dto.request.CreatePostRequest;
import com.huyhieu.postservice.dto.response.PostResponse;
import com.huyhieu.postservice.entity.Media;
import com.huyhieu.postservice.entity.Post;
import com.huyhieu.postservice.mapper.PostMapper;
import com.huyhieu.postservice.repository.PostRepository;
import com.huyhieu.postservice.service.FileStorageService;
import com.huyhieu.postservice.service.PostService;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
  PostRepository postRepository;
  PostMapper postMapper;
  FileStorageService fileStorageService;

  @Override
  public PostResponse createPost(CreatePostRequest request, List<MultipartFile> files) {
    List<Media> mediaList = getMediaList(files);

    String userId = CommonUtils.getCurrentUserId();
    Post post = postMapper.toPost(request);
    post.setUserId(userId);
    post.setMedia(mediaList);

    Post savedPost = postRepository.save(post);

    return postMapper.toPostResponse(enrichPostWithPresignedUrls(savedPost));
  }

  @NotNull
  private List<Media> getMediaList(List<MultipartFile> files) {
    List<Media> mediaList = new ArrayList<>();
    if (files != null && !files.isEmpty()) {
      for (MultipartFile file : files) {
        String fileUrl = fileStorageService.uploadFile(file); // Gọi MinIO
        Media media = Media.builder().url(fileUrl).type("IMAGE").build();
        mediaList.add(media);
      }
    }
    return mediaList;
  }

  @Override
  public PageResponse<PostResponse> getMyPosts(int page, int size) {
    String userId = CommonUtils.getCurrentUserId();
    Sort sort = Sort.by("createdDate").descending();
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Post> pageData = postRepository.findByUserId(userId, pageable);
    List<PostResponse> posts =
        pageData.stream()
            .map(this::enrichPostWithPresignedUrls)
            .map(postMapper::toPostResponse)
            .toList();

    return PageResponse.<PostResponse>builder()
        .currentPage(page)
        .pageSize(pageData.getSize())
        .totalPages(pageData.getTotalPages())
        .totalElements(pageData.getTotalElements())
        .data(posts)
        .build();
  }

  private Post enrichPostWithPresignedUrls(Post post) {
    if (post.getMedia() != null && !post.getMedia().isEmpty()) {
      post.getMedia()
          .forEach(
              media -> {
                // 'media.getUrl()' lúc này đang là objectName
                String presignedUrl = fileStorageService.getPresignedUrl(media.getUrl());
                // Ghi đè objectName bằng URL xem được
                media.setUrl(presignedUrl);
              });
    }
    return post;
  }
}
