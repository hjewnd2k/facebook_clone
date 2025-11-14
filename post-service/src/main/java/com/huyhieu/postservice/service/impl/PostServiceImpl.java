package com.huyhieu.postservice.service.impl;

import com.huyhieu.common.constants.KafkaTopics;
import com.huyhieu.common.dto.event.PostCreatedEvent;
import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostResponse;
import com.huyhieu.common.dto.response.PostStatsDTO;
import com.huyhieu.common.enums.Visibility;
import com.huyhieu.common.utils.CommonUtils;
import com.huyhieu.postservice.dto.request.CreatePostRequest;
import com.huyhieu.postservice.entity.Media;
import com.huyhieu.postservice.entity.Post;
import com.huyhieu.postservice.mapper.PostMapper;
import com.huyhieu.postservice.repository.InteractionServiceClient;
import com.huyhieu.postservice.repository.PostRepository;
import com.huyhieu.postservice.service.FileStorageService;
import com.huyhieu.postservice.service.PostService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {
  PostRepository postRepository;
  InteractionServiceClient interactionServiceClient;
  PostMapper postMapper;
  FileStorageService fileStorageService;
  KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public PostResponse createPost(CreatePostRequest request, List<MultipartFile> files) {
    List<Media> mediaList = getMediaList(files);

    String userId = CommonUtils.getCurrentUserId();
    Post post = postMapper.toPost(request);
    post.setUserId(userId);
    post.setMedia(mediaList);

    Post savedPost = postRepository.save(post);

    // check visibility = public
    if (savedPost.getVisibility() != Visibility.PRIVATE) {
      PostCreatedEvent event =
          PostCreatedEvent.builder()
              .createdDate(savedPost.getCreatedDate())
              .updatedDate(savedPost.getUpdatedDate())
              .postId(savedPost.getId())
              .authorId(savedPost.getUserId())
              .visibility(savedPost.getVisibility())
              .build();
      kafkaTemplate.send(KafkaTopics.POST_EVENTS, event);
    }

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
    List<String> postIds = pageData.getContent().stream().map(Post::getId).toList();
    Map<String, PostStatsDTO> statsMap =
        interactionServiceClient.getBatchPostStats(postIds).getResult();

    List<PostResponse> posts =
        pageData.stream().map(post -> getPostResponse(post, statsMap)).toList();

    return PageResponse.<PostResponse>builder()
        .currentPage(page)
        .pageSize(pageData.getSize())
        .totalPages(pageData.getTotalPages())
        .totalElements(pageData.getTotalElements())
        .data(posts)
        .build();
  }

  @Override
  public List<PostResponse> getPostsBatch(List<String> postIds) {
    if (postIds == null || postIds.isEmpty()) {
      return new ArrayList<>(); // Trả về list rỗng
    }

    List<Post> posts = postRepository.findAllById(postIds);

    Map<String, PostStatsDTO> statsMap =
        interactionServiceClient.getBatchPostStats(postIds).getResult();

    return posts.stream()
        .map(
            post -> {
              return getPostResponse(post, statsMap);
            })
        .toList();
  }

  @NotNull
  private PostResponse getPostResponse(Post post, Map<String, PostStatsDTO> statsMap) {
    Post enrichedPost = enrichPostWithPresignedUrls(post);
    PostResponse response = postMapper.toPostResponse(enrichedPost);
    PostStatsDTO stats = statsMap.get(post.getId());
    response.setLikeCount(stats.getLikeCount());
    response.setCommentCount(stats.getCommentCount());
    return response;
  }

  @Override
  public PageResponse<PostResponse> getColdFeed(List<String> authorIds, int page, int size) {
    if (authorIds == null || authorIds.isEmpty()) {
      return PageResponse.<PostResponse>builder().build();
    }

    Pageable sortedPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    Page<Post> postPage =
        postRepository.findByUserIdInAndVisibility(authorIds, Visibility.PUBLIC, sortedPageable);

    Map<String, PostStatsDTO> statsMap =
        interactionServiceClient.getBatchPostStats(authorIds).getResult();

    return PageResponse.<PostResponse>builder()
        .currentPage(page)
        .pageSize(postPage.getSize())
        .totalPages(postPage.getTotalPages())
        .totalElements(postPage.getTotalElements())
        .data(postPage.getContent().stream().map(post -> getPostResponse(post, statsMap)).toList())
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
