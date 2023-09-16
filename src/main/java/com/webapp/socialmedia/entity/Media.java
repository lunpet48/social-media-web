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
    @JoinColumn(name = "post_id")
    private Post postId;
    private String link;
    private String type;
}
