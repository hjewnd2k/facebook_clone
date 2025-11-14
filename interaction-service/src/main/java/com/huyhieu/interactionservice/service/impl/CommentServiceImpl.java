package com.huyhieu.interactionservice.service.impl;

import com.huyhieu.common.constants.KafkaTopics;
import com.huyhieu.common.dto.event.CommentCreatedEvent;
import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostStatsDTO;
import com.huyhieu.common.utils.CommonUtils;
import com.huyhieu.interactionservice.dto.request.CommentRequest;
import com.huyhieu.interactionservice.dto.response.CommentResponse;
import com.huyhieu.interactionservice.entity.Comment;
import com.huyhieu.interactionservice.mapper.CommentMapper;
import com.huyhieu.interactionservice.repository.CommentRepository;
import com.huyhieu.interactionservice.repository.ReactionRepository;
import com.huyhieu.interactionservice.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CommentServiceImpl implements CommentService {
  CommentRepository commentRepository;
  ReactionRepository reactionRepository;
  CommentMapper commentMapper;
  KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  @Transactional
  public CommentResponse createComment(String postId, CommentRequest request) {
    String userId = CommonUtils.getCurrentUserId();
    log.info("User {} bình luận vào post {}", userId, postId);
    Comment comment = commentMapper.toComment(request);
    comment.setPostId(postId);
    comment.setUserId(userId);

    Comment savedComment = commentRepository.save(comment);

    // 2. Gửi event KAFKA
    // TODO: Cần lấy 'postAuthorId'
    CommentCreatedEvent event = new CommentCreatedEvent(userId, postId, savedComment.getId());
    kafkaTemplate.send(KafkaTopics.COMMENT_EVENTS, event);

    log.info("Đã gửi event COMMENT_EVENTS cho post {}", postId);

    return commentMapper.toCommentResponse(savedComment);
  }

  @Override
  public PageResponse<CommentResponse> getCommentsForPost(String postId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

    Page<Comment> commentPage =
        commentRepository.findByPostIdAndParentCommentIdIsNullOrderByCreatedDateDesc(
            postId, pageable);

    return PageResponse.<CommentResponse>builder()
        .currentPage(page)
        .pageSize(size)
        .totalElements(commentPage.getTotalElements())
        .totalPages(commentPage.getTotalPages())
        .data(commentPage.getContent().stream().map(commentMapper::toCommentResponse).toList())
        .build();
  }

  @Override
  public Map<String, PostStatsDTO> getStatsForPosts(List<String> postIds) {
    // Đây là cách làm đơn giản (N+1 query)
    // (Để tối ưu hơn, bạn cần viết truy vấn Mongo Aggregation)

    return postIds.stream()
        .collect(
            Collectors.toMap(
                postId -> postId, // Key là postId
                postId -> { // Value là DTO
                  long commentCount = commentRepository.countByPostId(postId);
                  long reactionCount = reactionRepository.countByPostId(postId);

                  PostStatsDTO stats = new PostStatsDTO();
                  stats.setPostId(postId);
                  stats.setCommentCount(commentCount);
                  stats.setLikeCount(reactionCount); // (Hiện tại ta đếm tất cả reaction)
                  return stats;
                }));
  }
}
