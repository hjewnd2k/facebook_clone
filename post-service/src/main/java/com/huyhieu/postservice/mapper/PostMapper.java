package com.huyhieu.postservice.mapper;

import com.huyhieu.postservice.dto.request.CreatePostRequest;
import com.huyhieu.postservice.dto.response.PostResponse;
import com.huyhieu.postservice.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
  Post toPost(CreatePostRequest request);

  PostResponse toPostResponse(Post post);
}
