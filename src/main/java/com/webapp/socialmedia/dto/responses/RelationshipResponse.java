package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.enums.RelationshipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipResponse {
    private UserProfileResponse user;
    private UserProfileResponse userRelated;
    private RelationshipStatus status;

}

