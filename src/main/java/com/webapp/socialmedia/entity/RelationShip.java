package com.webapp.socialmedia.entity;

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
@IdClass(RelationShipId.class)
public class RelationShip {
    @Id
    @ManyToOne
    private User user1;
    @Id
    @ManyToOne
    private User user2;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        FRIEND,
        BLOCK
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class RelationShipId implements Serializable {
    private User user1;
    private User user2;
}
