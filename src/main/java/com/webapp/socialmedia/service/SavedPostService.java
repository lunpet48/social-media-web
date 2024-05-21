package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.responses.PostResponse;
import com.webapp.socialmedia.dto.responses.PostResponseV2;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.SavedPost;

import java.util.List;

public interface SavedPostService {
    SavedPost savePost(String postId);

    void unsavedPost(String postId);

    List<Post> getAllSavedPost();
}
