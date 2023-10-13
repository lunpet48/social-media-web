package com.webapp.socialmedia.entity;

import com.webapp.socialmedia.enums.PostMode;
import com.webapp.socialmedia.enums.PostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "db_post")
public class Post {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "text")
    private String caption;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostMode mode;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    private Date createdAt;

    @ManyToMany
    Set<Tag> tags;
    @PrePersist
    private void createdAt() {
        this.createdAt = new Date();
    }
}



