package com.huyhieu.interactionservice.service.impl;

import com.huyhieu.common.constants.KafkaTopics;
import com.huyhieu.common.dto.event.PostReactedEvent;
import com.huyhieu.common.enums.ReactionType;
import com.huyhieu.common.utils.CommonUtils;
import com.huyhieu.interactionservice.entity.Reaction;
import com.huyhieu.interactionservice.repository.ReactionRepository;
import com.huyhieu.interactionservice.service.ReactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ReactionServiceImpl implements ReactionService {
  ReactionRepository reactionRepository;
  KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void reactToPost(String postId, ReactionType reactionType) {
    String userId = CommonUtils.getCurrentUserId();

    // 1. Kiểm tra xem user đã react bài này chưa
    Optional<Reaction> existingReaction = reactionRepository.findByPostIdAndUserId(postId, userId);

    // 2. Sử dụng ifPresentOrElse để code rõ ràng hơn
    existingReaction.ifPresentOrElse(

        // === Kịch bản 1: Đã tồn tại (Update hoặc Delete) ===
        reaction -> {
          // Kịch bản 1a: Bấm lại (Toggle Off) -> XÓA
          // (Dùng '==' cho enums là an toàn và nhanh)
          if (reaction.getReactionType() == reactionType) {
            log.info("User {} bỏ react {} khỏi post {}", userId, reactionType, postId);
            reactionRepository.delete(reaction);
          } else {
            // Kịch bản 1b: Đổi reaction (Like -> Love) -> CẬP NHẬT
            log.info(
                "User {} đổi react từ {} sang {} cho post {}",
                userId,
                reaction.getReactionType(),
                reactionType,
                postId);
            reaction.setReactionType(reactionType);
            reactionRepository.save(reaction);

            // Gửi event KAFKA
            sendReactionEvent(userId, postId, reactionType);
          }
        },

        // === Kịch bản 2: React lần đầu (Create) ===
        () -> {
          log.info("User {} react {} cho post {}", userId, reactionType, postId);
          Reaction newReaction = createNewReaction(userId, postId, reactionType);
          reactionRepository.save(newReaction);

          // Gửi event KAFKA
          sendReactionEvent(userId, postId, reactionType);
        });
  }

  /**
   * Hàm helper (trợ giúp) để gửi event Kafka. Dọn dẹp TODO: Trách nhiệm tìm postAuthorId thuộc về
   * NotificationService.
   */
  private void sendReactionEvent(String userId, String postId, ReactionType reactionType) {
    PostReactedEvent event = new PostReactedEvent(userId, postId, reactionType);

    kafkaTemplate.send(KafkaTopics.REACTION_EVENTS, event);

    log.info("Đã gửi event REACTION_EVENTS (Type: {}) cho post {}", reactionType, postId);
  }

  /** Hàm helper (trợ giúp) để tạo object Reaction mới. */
  private Reaction createNewReaction(String userId, String postId, ReactionType reactionType) {
    Reaction newReaction = new Reaction();
    newReaction.setPostId(postId);
    newReaction.setUserId(userId);
    newReaction.setReactionType(reactionType);
    return newReaction;
  }
}
