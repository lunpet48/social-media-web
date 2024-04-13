package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.entity.Notification;
import com.webapp.socialmedia.repository.NotificationRepository;
import com.webapp.socialmedia.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    @Override
    public Notification saveNotiAndReturn(Notification notification) {
        return repository.saveAndFlush(notification);
    }
}
