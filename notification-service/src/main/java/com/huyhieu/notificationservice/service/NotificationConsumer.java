package com.huyhieu.notificationservice.service;

import com.huyhieu.common.constants.KafkaTopics;
import com.huyhieu.common.constants.WebSocketDestinations;
import com.huyhieu.common.dto.event.CommentCreatedEvent;
import com.huyhieu.common.dto.event.PostReactedEvent;
import com.huyhieu.common.enums.NotificationType;
import com.huyhieu.notificationservice.entity.Notification;
import com.huyhieu.notificationservice.mapper.NotificationMapper;
import com.huyhieu.notificationservice.repository.NotificationRepository;
import com.huyhieu.notificationservice.repository.PostServiceClient;
import com.huyhieu.notificationservice.repository.UserServiceClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationConsumer {

  PostServiceClient postServiceClient;
  UserServiceClient userServiceClient;
  NotificationRepository notificationRepository;
  SimpMessagingTemplate messagingTemplate; // Dùng để gửi WebSocket
  NotificationMapper notificationMapper;

  @KafkaListener(topics = KafkaTopics.REACTION_EVENTS, groupId = "notification-group")
  public void handleReaction(PostReactedEvent event) {
    log.info("Nhận được REACTION_EVENT từ {}", event.getReactorId());
    try {
      // 1. Lấy thông tin Tác giả bài viết
      String postAuthorId = postServiceClient.getPostAuthorId(event.getPostId());

      // 2. Không tự thông báo cho chính mình
      if (event.getReactorId().equals(postAuthorId)) {
        return;
      }

      // 3. Lấy tên người React
      String actorName = userServiceClient.getUserDisplayName(event.getReactorId());

      // 4. Tạo và Lưu thông báo
      Notification notif =
          createNotification(
              postAuthorId, // Người nhận
              event.getReactorId(), // Người thực hiện
              actorName,
              NotificationType.REACT_POST,
              event.getPostId());

      Notification savedNotif = notificationRepository.save(notif);

      // 5. Đẩy thông báo qua WebSocket
      // Gửi đến topic cá nhân của người nhận
      messagingTemplate.convertAndSendToUser(
          postAuthorId,
          WebSocketDestinations.NOTIFICATIONS, // FE sẽ lắng nghe topic này
          notificationMapper.toNotificationDTO(savedNotif));

    } catch (Exception e) {
      log.error("Lỗi khi xử lý REACTION_EVENT: {}", e.getMessage());
      // (Event này sẽ bị retry và vào DLT nếu cấu hình)
    }
  }

  @KafkaListener(topics = KafkaTopics.COMMENT_EVENTS, groupId = "notification-group")
  public void handleComment(CommentCreatedEvent event) {
    log.info("Nhận được COMMENT_EVENT từ {}", event.getCommenterId());
    try {
      // 1. Lấy thông tin Tác giả bài viết
      // TODO: (Dùng chung API với hàm trên)
      String postAuthorId = postServiceClient.getPostAuthorId(event.getPostId());

      // 2. Lấy tên người Bình luận
      String actorName = userServiceClient.getUserDisplayName(event.getCommenterId());

      // 3. Thông báo cho Tác giả bài viết (nếu không phải là tự bình luận)
      if (!event.getCommenterId().equals(postAuthorId)) {
        Notification notifForAuthor =
            createNotification(
                postAuthorId, // Người nhận
                event.getCommenterId(), // Người thực hiện
                actorName,
                NotificationType.COMMENT_POST,
                event.getPostId());
        Notification savedNotif = notificationRepository.save(notifForAuthor);
        sendNotificationToUser(postAuthorId, savedNotif);
      }

      // 4. TODO: Xử lý thông báo cho người bị trả lời (reply)
      // (Hiện tại DTO 'CommentCreatedEvent' chưa có 'parentCommentId')
      // if (event.getParentCommentId() != null) {
      //    String parentCommentAuthorId = ... // Gọi API của InteractionService
      //    if (!parentCommentAuthorId.equals(event.getCommenterId()) &&
      //        !parentCommentAuthorId.equals(postAuthorId)) {
      //        // Gửi thông báo thứ 2 cho người bị reply
      //    }
      // }

    } catch (Exception e) {
      log.error("Lỗi khi xử lý COMMENT_EVENT: {}", e.getMessage(), e);
      throw new RuntimeException("Lỗi xử lý COMMENT_EVENT", e);
    }
  }

  /** Hàm helper (trợ giúp) để tạo đối tượng Notification */
  private Notification createNotification(
      String userId, String actorId, String actorName, NotificationType type, String postId) {
    Notification notif = new Notification();
    notif.setUserId(userId); // Người nhận
    notif.setActorId(actorId);
    notif.setActorName(actorName);
    notif.setType(type);
    notif.setPostId(postId);
    notif.setRead(false);
    return notif;
  }

  /** Hàm helper (trợ giúp) để gửi WebSocket */
  private void sendNotificationToUser(String userId, Notification notification) {
    // Gửi đến topic cá nhân của người nhận
    messagingTemplate.convertAndSendToUser(
        userId, WebSocketDestinations.NOTIFICATIONS, notification);
  }
}
