package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Reaction;
import com.webapp.socialmedia.entity.ReactionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, ReactionId> {
    Optional<Reaction> findByPostIdAndUserId(String postId, String userId);
}
