package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_post_media")
public class PostMedia {
    @Id
    @Column(name = "media_id")
    private String mediaId;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "media_id")
    private Media media;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private Integer serial;
}
