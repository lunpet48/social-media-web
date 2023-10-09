package com.webapp.socialmedia.entity;

import com.webapp.socialmedia.enums.RelationshipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_relationship")
@IdClass(RelationshipId.class)
public class Relationship {
    @Id
    @ManyToOne
    private User user1;
    @Id
    @ManyToOne
    private User user2;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class RelationshipId implements Serializable {
    private User user1;
    private User user2;
}
