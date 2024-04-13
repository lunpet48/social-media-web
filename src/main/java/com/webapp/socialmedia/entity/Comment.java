package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_comment")
public class Comment {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "media_id")
    private Media media;

    @Column(columnDefinition = "text", nullable = false)
    private String comment;

    private Date createdAt;

    @ManyToOne
    private Comment repliedComment;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
