package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Log;
import com.webapp.socialmedia.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, String> {
    Log findByEventTypeAndObjectId(EventType eventType, String objectId);
}
