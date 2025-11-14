package com.huyhieu.interactionservice.mapper;

import com.huyhieu.interactionservice.dto.request.ReactionRequest;
import com.huyhieu.interactionservice.dto.response.ReactionResponse;
import com.huyhieu.interactionservice.entity.Reaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
  Reaction toReaction(ReactionRequest request);

  ReactionResponse toReactionResponse(Reaction reaction);
}
