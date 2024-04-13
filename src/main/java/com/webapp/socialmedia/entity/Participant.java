package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class ParticipantId implements Serializable {
    private User user;
    private Room room;
}