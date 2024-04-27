package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findAllByPostIdOrderByCreatedAt(String postId, Pageable pageable);
}
