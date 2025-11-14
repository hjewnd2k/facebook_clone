package com.huyhieu.interactionservice.service;

import com.huyhieu.common.enums.ReactionType;

public interface ReactionService {
  void reactToPost(String postId, ReactionType reactionType);
}
