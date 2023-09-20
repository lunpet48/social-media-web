package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_comment")
public class Comment {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(columnDefinition = "text", nullable = false)
    private String comment;

    private Date createdAt;

//    private Date updatedAt;

    private String repliedCommentId;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

//    @PreUpdate
//    void updatedAt() {
//        this.updatedAt = new Date();
//    }
}
