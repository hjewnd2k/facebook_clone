package com.huyhieu.userservice.entity;

import com.huyhieu.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
  @Column(name = "user_id")
  String userId; // Sẽ là ID (sub) từ Keycloak

  @Column(unique = true, nullable = false)
  String username; // Tên đăng nhập

  @Column(unique = true, name = "email")
  String email;

  @Column(name = "display_name")
  String displayName;

  @Column(name = "profile_pricture_url")
  String profilePictureUrl;

  String bio;
}
