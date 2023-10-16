package com.webapp.socialmedia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RelationshipId implements Serializable {
    @Column(name = "user_id")
    private String userId;
    @Column(name = "related_user_id")
    private String relatedUserId;
}
