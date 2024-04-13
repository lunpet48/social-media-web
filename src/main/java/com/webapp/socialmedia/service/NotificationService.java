package com.webapp.socialmedia.service;

import com.webapp.socialmedia.entity.Notification;
import com.webapp.socialmedia.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

public interface NotificationService {
    public Notification saveNotiAndReturn(Notification notification);
}
