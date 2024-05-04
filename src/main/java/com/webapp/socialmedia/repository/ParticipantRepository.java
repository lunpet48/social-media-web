package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Participant;
import com.webapp.socialmedia.entity.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId> {
    List<Participant> findParticipantByRoom_Id(String roomId);
}
