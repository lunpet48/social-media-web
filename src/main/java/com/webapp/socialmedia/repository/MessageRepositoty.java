package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepositoty extends JpaRepository<Message, String> {
    Page<Message> findByRoom_IdOrderByCreatedAtDesc(String roomId, Pageable pageable);
}
