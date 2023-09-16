package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_participant")
@IdClass(ParticipantId.class)
public class Participant {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @Id
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room roomId;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ParticipantId implements Serializable {
    private User userId;
    private Room roomId;
}