package com.videocall.server.repository;

import com.videocall.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String userName);
}
