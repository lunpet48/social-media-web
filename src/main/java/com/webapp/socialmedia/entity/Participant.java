package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_participant")
@IdClass(ParticipantId.class)
public class Participant {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ParticipantId implements Serializable {
    private User user;
    private Room room;
}