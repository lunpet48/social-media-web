package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.PostRequest;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.exceptions.PostNotFoundException;

import java.util.List;

public interface PostService {
    Post createPost(PostRequest postRequest);
    Post updatePost(Post post) throws PostNotFoundException;
}
