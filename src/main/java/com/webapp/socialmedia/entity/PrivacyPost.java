package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
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

    @Column(name = "view_permission")
    private Boolean viewPermission;
}
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class PrivacyPostId implements Serializable {
    private User user;
    private Post post;
}