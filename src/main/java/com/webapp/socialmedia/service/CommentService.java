package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.CommentRequest;
import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.entity.Media;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest commentRequest, Media media);

    void deleteComment(String commentId);

    List<CommentResponse> getComment(String postId);
}
