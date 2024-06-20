package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_reaction")
public class Reaction {
    @EmbeddedId
    ReactionId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    private Date createdAt;

    @PrePersist
    private void createdAt() {
        this.createdAt = new Date();
    }
}

