package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.CommentRequest;
import com.webapp.socialmedia.dto.responses.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest commentRequest);

    void deleteComment(String commentId);

    List<CommentResponse> getComment(String postId);
}
