package com.webapp.socialmedia.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class PostTagId implements Serializable {
    private String postId;
    private String tagId;
}
