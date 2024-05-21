package com.webapp.socialmedia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class SavedPostId implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "post_id")
    private String postId;
}
