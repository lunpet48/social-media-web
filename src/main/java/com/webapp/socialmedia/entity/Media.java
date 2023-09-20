package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_post_media")
public class Media {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType type;
}
