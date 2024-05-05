package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.NotificationRequest;
import com.webapp.socialmedia.dto.responses.NotificationResponse;
import com.webapp.socialmedia.entity.Notification;
import com.webapp.socialmedia.enums.NotificationStatus;
import com.webapp.socialmedia.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    public Notification saveNotiAndReturn(Notification notification);

    public List<NotificationResponse> findByReceiver(String id, Integer pageNo, Integer pageSize);

    public List<NotificationResponse> findByStatus(String id, NotificationStatus status, Integer pageNo, Integer pageSize);

    public Object getAnNotification(NotificationRequest notificationRequest);

    public Integer returnAmountOfNewNotification();

    public List<NotificationResponse> findByNotificationType(List<NotificationType> type, int pageNo, int pageSize);
}
