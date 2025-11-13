package com.huyhieu.newsfeedservice.service;

import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostResponse;

public interface FeedService {
  PageResponse<PostResponse> getMyFeed(int page, int size);
}
