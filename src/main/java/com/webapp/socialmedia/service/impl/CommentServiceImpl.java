package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.CommentRequest;
import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.entity.*;
import com.webapp.socialmedia.enums.NotificationType;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.exceptions.UserNotMatchTokenException;
import com.webapp.socialmedia.mapper.CommentMapper;
import com.webapp.socialmedia.mapper.NotificationMapper;
import com.webapp.socialmedia.repository.CommentRepository;
import com.webapp.socialmedia.repository.NotificationRepository;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.CommentService;
import com.webapp.socialmedia.utils.NotificationUtils;
import com.webapp.socialmedia.validattion.serviceValidation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserValidationService userValidationService;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    Pattern patternUser = Pattern.compile("@[A-z0-9_]+");
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

        //Thông báo tag người dùng khác
        Matcher matcher = patternUser.matcher(comment.getComment());
        while (matcher.find()) {
            String username = matcher.group().substring(1);
            User receiver = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
            if(!receiver.getId().equals(user.getId())) {
                Notification response = notificationRepository.saveAndFlush(Notification.builder()
                        .receiver(receiver)
                        .actor(user)
                        .idType(post.getId())
                        .notificationType(NotificationType.MENTION_IN_COMMENT)
                        .build());
                simpMessagingTemplate.convertAndSendToUser(username, NotificationUtils.NOTIFICATION_LINK, notificationMapper.toResponse(response));
            }
        }
        //Thông báo có người bình luận
        Comment response = commentRepository.saveAndFlush(comment);
        if(!comment.getPost().getUser().equals(user)) {
            var u = notificationRepository.saveAndFlush(Notification.builder()
                    .actor(user)
                    .receiver(comment.getPost().getUser())
                    .idType(post.getId())
                    .notificationType(NotificationType.COMMENT)
                    .build());
            simpMessagingTemplate.convertAndSendToUser(u.getReceiver().getUsername(), NotificationUtils.NOTIFICATION_LINK, notificationMapper.toResponse(u));
        }

        return commentMapper.toResponse(response);
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
    public List<CommentResponse> getComment(String postId, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedAt(postId, pageable);
        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = commentMapper.toResponse(comment);
            commentResponses.add(commentResponse);
        });
        return commentResponses;
    }

    @Override
    public CommentResponse getCommentById(String commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BadRequestException("Comment not found"));
        return commentMapper.toResponse(comment);
    }

}
