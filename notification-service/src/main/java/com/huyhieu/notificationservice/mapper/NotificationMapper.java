package com.huyhieu.notificationservice.mapper;

import com.huyhieu.common.dto.response.NotificationDTO;
import com.huyhieu.notificationservice.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
  NotificationDTO toNotificationDTO(Notification notification);
}
