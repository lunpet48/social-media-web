package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE u.id != :userId " +
            "AND u.role = 'USER' " +
            "AND u.id NOT IN (SELECT DISTINCT r.user.id FROM Relationship r WHERE r.relatedUser.id = :userId) " +
            "AND u.id NOT IN (SELECT DISTINCT r.relatedUser.id FROM Relationship r WHERE r.user.id = :userId)")
    List<User> getRecommendUsers(String userId);

    @Query("SELECT u FROM User u " +
            "WHERE u.username LIKE %:keyword% " +
            "AND u.role = 'USER' " +
            "OR u.id IN (SELECT p.userId FROM Profile p WHERE p.fullName LIKE %:keyword%)")
    List<User> searchUser(String keyword);
}
