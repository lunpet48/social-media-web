package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    Optional<Post> findByIdAndIsDeleted(String postId, Boolean isDeleted);

    List<Post> findByUser_IdAndIsDeletedOrderByCreatedAtAsc(String userId, Boolean isDeleted);

    @Query(value = "select * from db_post where user_id = ?1 and mode != 'PRIVATE' and is_deleted = false order by created_at", nativeQuery = true)
    List<Post> findPostsWithFriends(String userId);

    @Query(value = "select * from db_post where user_id = ?1 and mode = 'PUBLIC' and is_deleted = false order by created_at", nativeQuery = true)
    List<Post> findPostWithPublic(String userId);
}
