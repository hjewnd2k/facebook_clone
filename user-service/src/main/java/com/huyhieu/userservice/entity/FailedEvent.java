package com.huyhieu.userservice.entity;

import com.huyhieu.common.entity.BaseEntity;
import com.huyhieu.userservice.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "failed_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedEvent extends BaseEntity { // Kế thừa BaseEntity

  @Column(name = "original_topic")
  private String originalTopic;

  @Column(columnDefinition = "TEXT") // Dùng TEXT để lưu JSON dài
  private String payload;

  @Column(columnDefinition = "TEXT")
  private String errorMessage;

  @Column(columnDefinition = "TEXT")
  private String stackTrace;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private EventStatus status;
}
