package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_follower")
@IdClass(FollowerId.class)
public class Follower {
    @Id
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User followerUserId;
    @Id
    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followedUserId;
    private Date followedAt;

    @PrePersist
    void followedAt() {
        this.followedAt = new Date();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class FollowerId implements Serializable {
    private User followerUserId;
    private User followedUserId;
}
