package com.huyhieu.newsfeedservice.service.impl;

import com.huyhieu.common.dto.response.PageResponse;
import com.huyhieu.common.dto.response.PostResponse;
import com.huyhieu.common.utils.CommonUtils;
import com.huyhieu.newsfeedservice.constants.FeedConstants;
import com.huyhieu.newsfeedservice.dto.UserNodeDTO;
import com.huyhieu.newsfeedservice.repository.PostServiceClient;
import com.huyhieu.newsfeedservice.repository.SocialServiceClient;
import com.huyhieu.newsfeedservice.service.FeedService;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedServiceImpl implements FeedService {
  StringRedisTemplate redisTemplate;
  PostServiceClient postServiceClient;
  SocialServiceClient socialServiceClient;

    /**
     * Phương thức chính, điều phối việc lấy feed.
     * Ưu tiên 1: Lấy feed cá nhân (hot cache).
     * Ưu tiên 2: Lấy feed toàn cầu (warm cache).
     * Ưu tiên 3: Lấy feed từ DB (cold cache / cache miss).
     */
    @Override
    public PageResponse<PostResponse> getMyFeed(int page, int size) {
        String userId = CommonUtils.getCurrentUserId();
        long start = (long) (page - 1) * size;
        long end = ((long) page * size) - 1;

        // === KỊCH BẢN 1: Thử lấy Feed cá nhân (LIST) ===
        String personalFeedKey = String.format(FeedConstants.USER_FEED_KEY, userId);
        Long personalFeedSize = redisTemplate.opsForList().size(personalFeedKey);

        if (personalFeedSize != null && personalFeedSize > start) {
            log.info("Cache Hit (Personal): Trang {} của {}.", page, userId);
            return handlePersonalFeedHit(personalFeedKey, page, size, personalFeedSize, start, end);
        }

        // === KỊCH BẢN 2: Thử lấy Feed toàn cầu (ZSET) ===
        Long globalFeedSize = redisTemplate.opsForZSet().size(FeedConstants.GLOBAL_FEED_KEY);

        if (globalFeedSize != null && globalFeedSize > start) {
            log.info("Cache Hit (Global): Trang {} của {}.", page, userId);
            return handleGlobalFeedHit(page, size, globalFeedSize, start, end);
        }

        // === KỊCH BẢN 3: CACHE MISS (Fallback xuống DB) ===
        log.info(
                "Cache Miss: Cả 2 feed Redis (trang {}) của {} đều rỗng. Fallback DB.", page, userId);
        return handleCacheMiss(userId, page, size);
    }

    /**
     * Xử lý trường hợp Cache Hit, lấy từ Redis LIST (Feed cá nhân).
     */
    private PageResponse<PostResponse> handlePersonalFeedHit(
            String redisKey, int page, int size, long totalElements, long start, long end) {
        // 1. Lấy danh sách ID từ Redis
        List<String> postIds = redisTemplate.opsForList().range(redisKey, start, end);
        if (postIds == null || postIds.isEmpty()) {
            return createEmptyPage(page, size);
        }

        // 2. "Làm giàu" (Enrich) dữ liệu
        return getPostResponsePageResponse(page, size, totalElements, postIds);
    }

    private PageResponse<PostResponse> getPostResponsePageResponse(int page, int size, long totalElements, List<String> postIds) {
        List<PostResponse> postDetails = postServiceClient.getPostsBatch(postIds).getResult();

        // 3. Sắp xếp lại danh sách (Giải quyết TODO)
        List<PostResponse> sortedPosts = sortEnrichedPosts(postIds, postDetails);

        // 4. Tính toán phân trang
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // 5. Trả về PageResponse đầy đủ
        return buildPageResponse(page, size, totalElements, totalPages, sortedPosts);
    }

    /**
     * Xử lý trường hợp Cache Hit, lấy từ Redis ZSET (Feed toàn cầu).
     */
    private PageResponse<PostResponse> handleGlobalFeedHit(
            int page, int size, long totalElements, long start, long end) {
        // 1. Lấy danh sách ID từ Redis
        Set<String> globalPostIds =
                redisTemplate.opsForZSet().reverseRange(FeedConstants.GLOBAL_FEED_KEY, start, end);
        if (globalPostIds == null || globalPostIds.isEmpty()) {
            return createEmptyPage(page, size);
        }
        List<String> postIds = new ArrayList<>(globalPostIds);

        // 2. "Làm giàu" (Enrich) dữ liệu
        return getPostResponsePageResponse(page, size, totalElements, postIds);
    }

    /**
     * Xử lý trường hợp Cache Miss, lấy từ CSDL (Feed "lạnh").
     */
    private PageResponse<PostResponse> handleCacheMiss(String userId, int page, int size) {
        // 1. Lấy danh sách người đang follow
        List<UserNodeDTO> followingUsers =
                socialServiceClient.getFollowingForService(userId).getResult();
        if (followingUsers.isEmpty()) {
            log.info("User {} không follow ai, trả về feed rỗng.", userId);
            return createEmptyPage(page, size);
        }
        List<String> followingIds = followingUsers.stream().map(UserNodeDTO::getUserId).toList();

        // 2. Lấy PageResponse đầy đủ từ PostService (qua Feign)
        int pageZeroBased = page - 1;
        PageResponse<PostResponse> dbPageResponse =
                postServiceClient.getColdFeed(followingIds, pageZeroBased, size).getResult();

        // 3. Đảm bảo trang hiện tại (currentPage) là 1-based (page)
        dbPageResponse.setCurrentPage(page);

        return dbPageResponse;
    }

    /**
     * Helper: Sắp xếp danh sách PostResponse (unsorted)
     * theo đúng thứ tự của danh sách postIds (sorted) lấy từ Redis.
     */
    private List<PostResponse> sortEnrichedPosts(
            List<String> sortedPostIds, List<PostResponse> unsortedPosts) {
        // 1. Tạo Map để tra cứu O(1)
        Map<String, PostResponse> postMap =
                unsortedPosts.stream()
                        .collect(
                                Collectors.toMap(
                                        PostResponse::getId, // Giả định PostResponse có hàm getId()
                                        Function.identity(),
                                        (existing, replacement) -> existing // Xử lý trùng lặp nếu có
                                ));

        // 2. Lặp qua danh sách ID đã sắp xếp (từ Redis) và build lại danh sách
        return sortedPostIds.stream()
                .map(postMap::get)
                .filter(Objects::nonNull) // Lọc ra các post đã bị xóa (có ID trong cache nhưng DB không có)
                .toList();
    }

    /**
     * Helper: Tạo một PageResponse rỗng nhưng vẫn có metadata.
     */
    private PageResponse<PostResponse> createEmptyPage(int page, int size) {
        return PageResponse.<PostResponse>builder().currentPage(page).pageSize(size).build();
    }

    /**
     * Helper: Xây dựng PageResponse.
     */
    private PageResponse<PostResponse> buildPageResponse(
            int page, int size, long totalElements, int totalPages, List<PostResponse> data) {
        return PageResponse.<PostResponse>builder()
                .data(data)
                .pageSize(size)
                .currentPage(page)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }
}
