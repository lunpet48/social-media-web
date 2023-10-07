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
@Table(name = "db_privacy_post")
@IdClass(PrivacyPostId.class)
public class PrivacyPost {
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
class PrivacyPostId implements Serializable {
    private User user;
    private Post post;
}