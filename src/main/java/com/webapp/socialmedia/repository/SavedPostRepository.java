package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.SavedPost;
import com.webapp.socialmedia.entity.SavedPostId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedPostRepository extends JpaRepository<SavedPost, SavedPostId> {
    List<SavedPost> findByUser_IdAndPost_IsDeletedOrderByCreatedAt(String userId, Boolean isDeleted);
}
