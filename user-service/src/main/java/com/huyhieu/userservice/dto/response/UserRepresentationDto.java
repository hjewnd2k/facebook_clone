package com.huyhieu.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRepresentationDto {
  private String username;
  private String email;
  private String firstName;
  private String lastName;
}
