package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Reaction;
import com.webapp.socialmedia.entity.ReactionId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, ReactionId> {
    Optional<Reaction> findByPostIdAndUserId(String postId, String userId);

    List<Reaction> findByUser_Id(String userId, Pageable pageable);
}
