package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.responses.ReactionResponse;
import com.webapp.socialmedia.entity.User;

public interface ReactionService {
    ReactionResponse likePost(String postId, User user);

    ReactionResponse dislikePost(String postId, User user);

    ReactionResponse getReaction(String postId, User user);
}
