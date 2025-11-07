package com.huyhieu.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
  private int statusCode;
  private String message;
  private LocalDateTime timestamp;
  private String path;
}
