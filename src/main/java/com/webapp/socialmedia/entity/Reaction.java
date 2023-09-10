package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_reaction")
@IdClass(ReactionId.class)
public class Reaction {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ReactionId implements Serializable{
    private User userId;
    private Post postId;
}
