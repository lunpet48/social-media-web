package com.webapp.socialmedia.entity;

import com.webapp.socialmedia.enums.RelationshipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_relationship")
//@IdClass(RelationshipId.class)
public class Relationship {
    @EmbeddedId
    RelationshipId id;

    @ManyToOne
    @MapsId("user1Id")
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @MapsId("user2Id")
    @JoinColumn(name = "user2_id")
    private User user2;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;
}


