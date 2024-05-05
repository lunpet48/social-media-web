package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Notification;
import com.webapp.socialmedia.enums.NotificationStatus;
import com.webapp.socialmedia.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    Page<Notification> findAllByReceiver_IdOrderByCreatedAtDesc(String id, Pageable pageable);

    Page<Notification> findAllByReceiver_IdAndStatusOrderByCreatedAtDesc(String id, NotificationStatus status, Pageable pageable);

    List<Notification> findAllByReceiver_IdAndStatusOrderByCreatedAtDesc(String id, NotificationStatus status);

    List<Notification> findAllByReceiver_IdAndNotificationTypeInOrderByCreatedAtDesc(String receiverId, List<NotificationType> type, Pageable pageable);
}
