package com.webapp.socialmedia.entity;

import com.webapp.socialmedia.enums.PostMode;
import com.webapp.socialmedia.enums.PostType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;


@Getter
@Setter
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

    @Builder.Default
    private Boolean isDeleted = false;

    private Date createdAt;

    @OneToMany(mappedBy = "post")
    List<PostTag> postTags;

    @PrePersist
    private void createdAt() {
        this.createdAt = new Date();
    }
}



