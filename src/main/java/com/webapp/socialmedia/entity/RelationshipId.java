package com.webapp.socialmedia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RelationshipId implements Serializable {
    @Column(name = "user1_id")
    private String user1Id;
    @Column(name = "user2_id")
    private String user2Id;
}
