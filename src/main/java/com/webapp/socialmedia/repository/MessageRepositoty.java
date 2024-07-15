package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageRepositoty extends JpaRepository<Message, String> {
    Page<Message> findByRoom_IdOrderByCreatedAtDesc(String roomId, Pageable pageable);

    Optional<Message> findTopByRoomIdOrderByCreatedAtDesc(String roomId);

    @Query(value = "SELECT db_participant.room_id, MAX(db_message.created_at) AS createdAt FROM db_participant LEFT JOIN db_message ON db_participant.room_id = db_message.room_id WHERE db_participant.user_id = ?1 GROUP BY db_participant.room_id ORDER BY createdAt DESC", nativeQuery = true)
    List<Map<String, Object>> loadRoomsByUserId(String userId);

    Optional<Message> findByRoom_IdAndCreatedAt(String roomId, Date createdAt);
}
