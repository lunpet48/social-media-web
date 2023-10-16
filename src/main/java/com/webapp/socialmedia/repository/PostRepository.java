package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    Optional<Post> findByIdAndIsDeleted(String postId, Boolean isDeleted);
}
