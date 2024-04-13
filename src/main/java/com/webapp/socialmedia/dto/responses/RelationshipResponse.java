package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.enums.RelationshipStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipResponse {
    private UserProfileResponse user;
    private UserProfileResponse userRelated;
    private RelationshipStatus status;

}

