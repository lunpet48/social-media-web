package com.webapp.socialmedia.dto.requests;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipRequest {
    private String targetId;
}
