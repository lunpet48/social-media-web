package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.dto.responses.UserResponse;
import com.webapp.socialmedia.entity.Post;

public interface AdminService {
    Post findPostById(String postId);
    CommentResponse findCommentById(String commentId);
    UserProfileResponse findUserById(String userId);
}
