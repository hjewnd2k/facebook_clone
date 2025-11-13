package com.huyhieu.newsfeedservice.event;

import com.huyhieu.common.constants.KafkaTopics;
import com.huyhieu.common.dto.event.PostCreatedEvent;
import com.huyhieu.common.dto.response.ApiResponse;
import com.huyhieu.common.enums.Visibility;
import com.huyhieu.newsfeedservice.constants.FeedConstants;
import com.huyhieu.newsfeedservice.dto.UserNodeDTO;
import com.huyhieu.newsfeedservice.repository.SocialServiceClient;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KafkaFeedConsumer {

  StringRedisTemplate redisTemplate;
  SocialServiceClient socialServiceClient;

  @KafkaListener(topics = KafkaTopics.POST_EVENTS, groupId = "newsfeed-group")
  public void handlePostCreated(PostCreatedEvent event) {
    log.info("Nhận được event tạo post: {}", event.getPostId());

    String authorId = event.getAuthorId();
    String postId = event.getPostId();

    try {
      ApiResponse<List<UserNodeDTO>> response =
          socialServiceClient.getFollowersForService(authorId);
      List<UserNodeDTO> followers = response.getResult();

      if (followers != null && !followers.isEmpty()) {
        for (UserNodeDTO follower : followers) {
          String redisKey = String.format(FeedConstants.USER_FEED_KEY, follower.getUserId());
          log.info("Đẩy post {} vào feed của {}", postId, follower.getUserId());
          redisTemplate.opsForList().leftPush(redisKey, postId);
          // 2. CẮT BỚT: Chỉ giữ lại 500 tin (index 0 đến 499)
          redisTemplate.opsForList().trim(redisKey, 0, FeedConstants.MAX_FEED_SIZE - 1);
        }
      }

      if (event.getVisibility() == Visibility.FRIENDS) {
        return;
      }
      double score = (double) event.getCreatedDate().toEpochMilli();

      redisTemplate.opsForZSet().add(FeedConstants.GLOBAL_FEED_KEY, postId, score);
      log.info("Đã thêm post {} vào Global Feed", postId);

      // CẮT BỚT: Giữ lại 500 tin mới nhất (ZREMRANGEBYRANK)
      redisTemplate
          .opsForZSet()
          .removeRange(FeedConstants.GLOBAL_FEED_KEY, 0, -(FeedConstants.MAX_FEED_SIZE + 1));
    } catch (Exception e) {
      log.error("Lỗi khi xử lý event tạo post: {}", event.getPostId(), e);
    }
  }
}
