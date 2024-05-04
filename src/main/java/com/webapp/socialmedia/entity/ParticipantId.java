package com.webapp.socialmedia.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantId implements Serializable {
    private User user;
    private Room room;
}