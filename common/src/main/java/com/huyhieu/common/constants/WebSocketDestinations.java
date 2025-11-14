package com.huyhieu.common.constants;

/** Nơi định nghĩa các "đích đến" (Destinations) cho WebSocket STOMP. */
public final class WebSocketDestinations {
  /** Tiền tố (prefix) mà client gửi message ĐẾN server. Ví dụ: /app/chat */
  public static final String APP_PREFIX = "/app";

  /**
   * Tiền tố (prefix) mà server gửi message ĐẾN client. Dùng cho các topic chung (broadcast). Ví dụ:
   * /topic/public-chat
   */
  public static final String TOPIC_PREFIX = "/topic";

  /**
   * Tiền tố (prefix) mà server gửi message CÁ NHÂN đến client. Ví dụ:
   * /user/{userId}/queue/notifications (Spring sẽ tự động thay /user/ thành /user/{userId})
   */
  public static final String USER_PREFIX = "/user";

  /**
   * Đích đến (destination) cụ thể cho thông báo cá nhân. Client sẽ lắng nghe (subscribe) tại:
   * /user/queue/notifications Server sẽ gửi (send) đến: /queue/notifications (và chỉ định userId)
   */
  public static final String NOTIFICATIONS = "/queue/notifications";
}
