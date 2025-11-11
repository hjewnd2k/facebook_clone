package com.huyhieu.userservice.enums;

public enum EventStatus {
  PENDING, // Mới, đang chờ xử lý
  REPROCESSED, // Đã xử lý lại (thành công)
  IGNORED // Bỏ qua (lỗi không quan trọng)
}
