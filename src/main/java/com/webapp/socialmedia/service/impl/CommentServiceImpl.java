package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.CommentRequest;
import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.entity.Comment;
import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.UserNotMatchTokenException;
import com.webapp.socialmedia.mapper.CommentMapper;
import com.webapp.socialmedia.repository.CommentRepository;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.service.CommentService;
import com.webapp.socialmedia.validattion.serviceValidation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserValidationService userValidationService;
    private final PostRepository postRepository;

    @Override
    public CommentResponse createComment(CommentRequest commentRequest, Media media){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // kiểm tra post tồn tại
        Post post = postRepository.findByIdAndIsDeleted(commentRequest.getPostId(), false)
                .orElseThrow(() ->new BadRequestException("Post Not Found"));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .comment(commentRequest.getComment())
                .media(media.getId() != null ? media : null)
                .repliedComment(commentRepository.findById(commentRequest.getRepliedCommentId()).orElse(null))
                .build();
        //media

        // replied comment

        commentRepository.save(comment);
        return CommentMapper.INSTANCE.toResponse(comment);
    }

    @Override
    public void deleteComment(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new BadRequestException("Comment not found"));

        // kiểm tra người xóa (current user) có phải là chủ nhân của comment
        if(!userValidationService.isUserMatchToken(comment.getUser().getId()))
            throw new UserNotMatchTokenException();

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponse> getComment(String postId) {
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAt(postId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = CommentMapper.INSTANCE.toResponse(comment);
            commentResponses.add(commentResponse);
        });
        return commentResponses;
    }

    @Override
    public CommentResponse getCommentById(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BadRequestException("Comment not found"));
        return CommentMapper.INSTANCE.toResponse(comment);
    }

}
