package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.NotificationRequest;
import com.webapp.socialmedia.dto.responses.NotificationResponse;
import com.webapp.socialmedia.entity.Notification;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.NotificationStatus;
import com.webapp.socialmedia.enums.NotificationType;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.mapper.NotificationMapper;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.repository.NotificationRepository;
import com.webapp.socialmedia.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final PostService postService;
    private final PostMediaService postMediaService;
    private final PostMapper postMapper;
    private final IProfileService profileService;
    private final CommentService commentService;
    @Override
    public Notification saveNotiAndReturn(Notification notification) {
        return repository.saveAndFlush(notification);
    }

    public List<NotificationResponse> findByReceiver(String id, Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);

        Page<Notification> pageResult = repository.findAllByReceiver_IdOrderByCreatedAtDesc(id, paging);

        return pageResult.getContent().stream().map(mapper::toResponse).toList();
//        if(pageResult.hasContent()) {
//            pageResult.getContent().forEach(x -> x.setStatus(NotificationStatus.READ));
//            repository.saveAllAndFlush(pageResult);
//            return pageResult.getContent().stream().map(mapper::toResponse).toList();
//        } else {
//            return new ArrayList<>();
//        }
    }

    @Override
    public List<NotificationResponse> findByStatus(String id, NotificationStatus status, Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);

        Page<Notification> pageResult = repository.findAllByReceiver_IdAndStatusOrderByCreatedAtDesc(id, status, paging);

        return pageResult.getContent().stream().map(mapper::toResponse).toList();
//        if(pageResult.hasContent()) {
//            pageResult.getContent().forEach(x -> x.setStatus(NotificationStatus.READ));
//            repository.saveAllAndFlush(pageResult);
//            return pageResult.getContent().stream().map(mapper::toResponse).toList();
//        } else {
//            return new ArrayList<>();
//        }
    }

    @Override
    public Object getAnNotification(String id) {
        Notification temp = repository.findById(id).orElseThrow(() -> new BadRequestException("Thông báo không tồn tại"));
        temp.setStatus(NotificationStatus.READ);
        repository.save(temp);
        if(temp.getNotificationType().equals(NotificationType.FRIEND_REQUEST) || temp.getNotificationType().equals(NotificationType.FRIEND_ACCEPT)) {
            //Trả về trang cá nhân của người gửi
            return profileService.get(temp.getActor().getId());
        }
        else if(temp.getNotificationType().equals(NotificationType.LIKE)) {
            //Trả về bài viết
            Post post = postService.getPost(temp.getIdType(), temp.getReceiver().getId());
            List<PostMedia> postMediaList = postMediaService.getFilesByPostId(temp.getIdType());
            return postMapper.toResponse(post, postMediaList);
        }
        else if(temp.getNotificationType().equals(NotificationType.COMMENT)) {
            //Trả về comment
            return commentService.getCommentById(temp.getIdType());
        } else {
            throw new BadRequestException("Thông báo không tồn tại");
        }
    }

    @Override
    public Integer returnAmountOfNewNotification() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findAllByReceiver_IdAndStatusOrderByCreatedAtDesc(user.getId(), NotificationStatus.UNREAD).size();
    }

    @Override
    public List<NotificationResponse> findByNotificationType(List<NotificationType> type, int pageNo, int pageSize) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable paging = PageRequest.of(pageNo, pageSize);
        List<Notification> result = repository.findAllByReceiver_IdAndNotificationTypeInOrderByCreatedAtDesc(user.getId(), type, paging);
        return result.stream().map(mapper::toResponse).toList();
    }
}
