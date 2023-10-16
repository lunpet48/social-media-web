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
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("relatedUserId")
    @JoinColumn(name = "related_user_id")
    private User relatedUser;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;
}


