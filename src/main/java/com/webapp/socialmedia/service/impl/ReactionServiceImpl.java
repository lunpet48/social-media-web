package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.responses.ReactionResponse;
import com.webapp.socialmedia.entity.*;
import com.webapp.socialmedia.enums.NotificationType;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.mapper.NotificationMapper;
import com.webapp.socialmedia.repository.NotificationRepository;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.ReactionRepository;
import com.webapp.socialmedia.service.ReactionService;
import com.webapp.socialmedia.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public ReactionResponse likePost(String postId, User user){
        // thiếu kiểm tra user có quyền xem bài viết không
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kiểm tra bài viết có tồn tại không
        Post post = postRepository.findByIdAndIsDeleted(postId, false)
                .orElseThrow(() ->new BadRequestException("Post Not Found"));

        //Kiểm tra đã like hay chưa
        Optional<Reaction> reactionExist = reactionRepository.findByPostIdAndUserId(post.getId(), user.getId());
        if(reactionExist.isPresent())
            throw new BadRequestException("You already like this post");

        ReactionId id = new ReactionId();
        id.setUserId(user.getId());
        id.setPostId(post.getId());

        Reaction reaction = Reaction.builder()
                .id(id)
                .user(user)
                .post(post)
                .build();
        //Thông báo (id type lưu id bài viết)
        Reaction newReaction = reactionRepository.saveAndFlush(reaction);
        if(!currentUser.getId().equals(post.getUser().getId())) {
            Notification notification = Notification.builder()
                    .actor(newReaction.getUser())
                    .receiver(newReaction.getPost().getUser())
                    .notificationType(NotificationType.LIKE)
                    .idType(newReaction.getPost().getId())
                    .build();
            notificationRepository.saveAndFlush(notification);
            simpMessagingTemplate.convertAndSendToUser(notification.getReceiver().getUsername(), NotificationUtils.NOTIFICATION_LINK, notificationMapper.toResponse(notification));
        }

        return ReactionResponse.builder()
                .userId(user.getId())
                .postId(post.getId())
                .liked(true)
                .build();
    }

    @Override
    public ReactionResponse dislikePost(String postId, User user){
        // thiếu kiểm tra user có quyền xem bài viết không

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->new BadRequestException("Post Not Found"));

        Reaction reaction = reactionRepository.findByPostIdAndUserId(post.getId(), user.getId())
                .orElseThrow(()-> new BadRequestException("Not Found"));

        reactionRepository.delete(reaction);

        return ReactionResponse.builder()
                .userId(user.getId())
                .postId(post.getId())
                .liked(false)
                .build();
    }

    @Override
    public ReactionResponse getReaction(String postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->new BadRequestException("Post Not Found"));

        boolean isLiked = reactionRepository.findByPostIdAndUserId(post.getId(), user.getId()).isPresent();

        return ReactionResponse.builder()
                .userId(user.getId())
                .postId(post.getId())
                .liked(isLiked)
                .build();
    }

    @Override
    public List<Post> getLikedPosts(int pageSize, int pageNo) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reaction> reactions = reactionRepository.findByUser_IdOrderByCreatedAtDesc(user.getId(), paging);
        List<Post> posts = new ArrayList<>();

        reactions.forEach(reaction -> {
            posts.add(reaction.getPost());
        });
        return posts;
    }
}
