package com.huyhieu.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.huyhieu.userservice.enums.OperationType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakEventDto {
  private OperationType type;
  private String userId;
  private KeycloakEventDetailsDto details;
}
