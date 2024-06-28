package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostMediaRepository extends JpaRepository<PostMedia, String> {
    @Query(value = "Select * from db_post_media where post_id = ? order by serial", nativeQuery = true)
    List<PostMedia> findByPostIdOrderBySerial(String postId);

    @Transactional
    @Modifying
    @Query(value = "Delete from db_post_media where media_id = ?", nativeQuery = true)
    void deleteOnlyPostMedia(String mediaId);
}
