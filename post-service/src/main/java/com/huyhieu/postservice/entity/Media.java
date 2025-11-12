package com.huyhieu.postservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {
  private String type; // "IMAGE" hoặc "VIDEO"
  private String url; // Đường dẫn (URL) đến file trên MinIO/S3
}
