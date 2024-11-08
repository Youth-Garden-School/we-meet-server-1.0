package com.videocall.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.videocall.server.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String username);

    boolean existsByUserName(String userName);

    @Query("SELECT u.id FROM User u WHERE u.userName = :username")
    String findIdByUserName(@Param("username") String username);
}
