package com.huyhieu.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.huyhieu.userservice.enums.OperationType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminEventDto {
  private String id;
  private String resourceId;
  private long time;
  private String realmName;
  private String resourceType; // Sẽ là "USER"
  private OperationType operationType; // Sẽ là "CREATE", "UPDATE"...
  private String resourcePath; // Chứa ID của user
  private String representation; // Đây là JSON string
}
