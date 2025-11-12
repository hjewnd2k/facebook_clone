package com.huyhieu.socialservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("User")
@Data
public class UserNode {
  @Id private final String userId; // Dùng ID (sub) từ Keycloak làm ID
}
