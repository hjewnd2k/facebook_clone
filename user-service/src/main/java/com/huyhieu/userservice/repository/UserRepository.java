package com.huyhieu.userservice.repository;

import com.huyhieu.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserId(String userId);

  void deleteByUserId(String userId);
}
