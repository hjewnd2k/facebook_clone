package com.huyhieu.socialservice.repository;

import com.huyhieu.socialservice.entity.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialRepository extends Neo4jRepository<UserNode, String> {

  // Đảm bảo node người dùng tồn tại (tương tự JIT Provisioning)
  // 'MERGE' có nghĩa là: "tìm, nếu không có thì tạo"
  @Query("MERGE (u:User {userId: $userId}) RETURN u")
  UserNode mergeUser(@Param("userId") String userId);

  // Tạo một quan hệ :FOLLOWS
  @Query(
      "MERGE (a:User {userId: $followerId}) "
          + "MERGE (b:User {userId: $followingId}) "
          + "MERGE (a)-[r:FOLLOWS]->(b)")
  void followUser(@Param("followerId") String followerId, @Param("followingId") String followingId);

  // Hủy theo dõi
  @Query(
      "MATCH (a:User {userId: $followerId})-[r:FOLLOWS]->(b:User {userId: $followingId}) "
          + "DELETE r")
  void unfollowUser(
      @Param("followerId") String followerId, @Param("followingId") String followingId);

  // Lấy danh sách người mà BẠN đang follow (Following)
  @Query("MATCH (a:User {userId: $userId})-[:FOLLOWS]->(b:User) " + "RETURN b")
  List<UserNode> findFollowing(@Param("userId") String userId);

  // Lấy danh sách người đang follow BẠN (Followers)
  @Query("MATCH (a:User)-[:FOLLOWS]->(b:User {userId: $userId}) " + "RETURN a")
  List<UserNode> findFollowers(@Param("userId") String userId);

  // Lấy BẠN CHUNG (Mutual Friends/Follows)
  // (Những người mà CẢ BẠN VÀ targetUserId CÙNG FOLLOW)
  @Query(
      "MATCH (a:User {userId: $userId})-[:FOLLOWS]->(mutual:User)<-[:FOLLOWS]-(b:User {userId: $targetUserId}) "
          + "RETURN mutual")
  List<UserNode> findMutualFollows(
      @Param("userId") String userId, @Param("targetUserId") String targetUserId);
}
