package com.huyhieu.interactionservice.mapper;

import com.huyhieu.interactionservice.dto.request.CommentRequest;
import com.huyhieu.interactionservice.dto.response.CommentResponse;
import com.huyhieu.interactionservice.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
  Comment toComment(CommentRequest commentRequest);

  CommentResponse toCommentResponse(Comment comment);
}
