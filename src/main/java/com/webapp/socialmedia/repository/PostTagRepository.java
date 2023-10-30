package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.PostTag;
import com.webapp.socialmedia.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
    void deletePostTagsById_PostId(String postId);
}
