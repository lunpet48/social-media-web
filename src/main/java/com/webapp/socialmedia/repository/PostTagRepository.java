package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.PostTag;
import com.webapp.socialmedia.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
    void deletePostTagsById_PostId(String postId);

    @Query("SELECT pt FROM PostTag pt WHERE pt.tag.id = :tagName")
    List<PostTag> findByTagName(String tagName);
}
