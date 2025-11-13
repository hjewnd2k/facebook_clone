package com.huyhieu.common.constants;

public final class KafkaTopics {

  // Private constructor để ngăn ai đó tạo instance (new) của class này
  private KafkaTopics() {}

  /**
   * Topic cho các sự kiện (Admin) từ Keycloak (CREATE, UPDATE, DELETE user). Được gửi bởi: Keycloak
   * Plugin Được nghe bởi: user-service
   */
  public static final String ADMIN_EVENTS = "keycloak_admin_events";

  /**
   * Topic DLT (Dead Letter Topic) cho các event Admin bị lỗi. Được gửi bởi: user-service (Error
   * Handler) Được nghe bởi: user-service (DLT Logger)
   */
  public static final String ADMIN_EVENTS_DLT = "keycloak_admin_events-dlt";

  // (Chúng ta thống nhất dùng tên -dlt)

  /**
   * Topic cho các sự kiện từ Post Service (ví dụ: POST_CREATED). Được gửi bởi: post-service Được
   * nghe bởi: news-feed-service
   */
  public static final String POST_EVENTS = "post_events";

  // (Sau này bạn có thể thêm: NOTIFICATION_EVENTS, CHAT_EVENTS, v.v.)
}
