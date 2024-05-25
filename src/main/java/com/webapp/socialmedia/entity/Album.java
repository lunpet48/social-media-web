package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "db_album")
public class Album {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;

    private Date createdAt;

    @Builder.Default
    private Boolean isDeleted = false;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();

    }
}
