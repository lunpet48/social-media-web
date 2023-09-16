package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_post")
public class Post {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @Column(columnDefinition = "text")
    private String caption;
}
