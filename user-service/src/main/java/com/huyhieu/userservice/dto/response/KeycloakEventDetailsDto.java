package com.huyhieu.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakEventDetailsDto {
  private String username;
  private String email;
}
