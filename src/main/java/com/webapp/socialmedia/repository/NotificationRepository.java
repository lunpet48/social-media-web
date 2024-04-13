package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {

}
