package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    Optional<Post> findByIdAndIsDeleted(String postId, Boolean isDeleted);

    List<Post> findByUser_IdAndIsDeletedOrderByCreatedAtDesc(String userId, Boolean isDeleted);

    List<Post> findByAlbum_IdAndIsDeletedOrderByCreatedAtDesc(String albumId, Boolean isDeleted);

    @Query(value = "select * from db_post where user_id = ?1 and mode != 'PRIVATE' and is_deleted = false order by created_at DESC", nativeQuery = true)
    List<Post> findPostsWithFriends(String userId);

    @Query(value = "select * from db_post where user_id = ?1 and album_id = ?2 and mode != 'PRIVATE' and is_deleted = false order by created_at DESC", nativeQuery = true)
    List<Post> findPostsWithFriendsInAlbum(String userId, String albumId);

    @Query(value = "select * from db_post where user_id = ?1 and mode = 'PUBLIC' and is_deleted = false order by created_at DESC", nativeQuery = true)
    List<Post> findPostWithPublic(String userId);

    @Query(value = "select * from db_post where user_id = ?1 and album_id = ?2 and mode = 'PUBLIC' and is_deleted = false order by created_at DESC", nativeQuery = true)
    List<Post> findPostWithPublicInAlbum(String userId, String albumId);

    @Query(value = "select * from db_post where user_id = ?1 and mode != 'PRIVATE' and is_deleted = false and datediff(now(), created_at) <= ?2 LIMIT ?3 OFFSET ?4", nativeQuery = true)
    List<Post> findPostsWithFriendsAndDay(String userId, int day, int pageSize, int pageNo);

    @Query(value = "select * from db_post where user_id = ?1 and mode = 'PUBLIC' and is_deleted = false and shared_post_id is not null order by created_at DESC", nativeQuery = true)
    List<Post> findSharedPostWithPublic(String userId);

    @Query(value = "select * from db_post where user_id = ?1 and mode != 'PRIVATE' and is_deleted = false and shared_post_id is not null order by created_at DESC", nativeQuery = true)
    List<Post> findSharedPostsWithFriends(String userId);

    List<Post> findByUser_IdAndSharedPostIsNotNullAndIsDeletedOrderByCreatedAtDesc(String user_id, Boolean isDeleted);

@Query("SELECT p FROM Post p " +
        "WHERE p.caption LIKE %:keyword% " +
        "AND (p.mode = 'PUBLIC' OR " +
        "(p.mode = 'FRIEND' AND " +
        ":userId IN " +
        "(SELECT r.user.id FROM Relationship r " +
        "WHERE r.relatedUser.id = p.user.id AND r.status = 'FRIEND')))")
    List<Post> searchPost(String keyword, String userId);
}
