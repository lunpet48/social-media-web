package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.responses.ReactionResponse;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.User;

import java.util.List;

public interface ReactionService {
    ReactionResponse likePost(String postId, User user);

    ReactionResponse dislikePost(String postId, User user);

    ReactionResponse getReaction(String postId, User user);

    List<Post> getLikedPosts(int pageSize, int pageNo);
}
