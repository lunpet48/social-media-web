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
@Table(name = "db_reaction")
@IdClass(ReactionId.class)
public class Reaction {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ReactionId implements Serializable{
    private User user;
    private Post post;
}
