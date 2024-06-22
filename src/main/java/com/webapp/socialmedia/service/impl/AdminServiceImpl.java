package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.dto.responses.UserResponse;
import com.webapp.socialmedia.entity.Comment;
import com.webapp.socialmedia.entity.Log;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.mapper.CommentMapper;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.CommentRepository;
import com.webapp.socialmedia.repository.LogRepository;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final UserMapper userMapper;

    @Override
    public Post findPostById(String postId) {
        return postRepository.findById(postId).orElseThrow(() -> new BadRequestException("Không tìm thấy bài viết!!"));
    }

    @Override
    public CommentResponse findCommentById(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BadRequestException("Không tìm thấy bình luận!!"));
        return commentMapper.toResponse(comment);
    }

    @Override
    public UserProfileResponse findUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userMapper.userToUserProfileResponse(user);
    }

    @Override
    public Log findLogById(String logId) {
        return logRepository.findById(logId).orElseThrow(() -> new BadRequestException("Không tìm thấy hành động này"));
    }
}
