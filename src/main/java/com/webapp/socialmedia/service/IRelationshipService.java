package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.RelationshipRequest;
import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.enums.RelationshipStatus;

import java.util.List;

public interface IRelationshipService {
    RelationshipResponse sendFriendRequest(RelationshipRequest relationshipRequest);

    void cancelFriendRequest(RelationshipRequest relationshipRequest);

    RelationshipResponse acceptFriendRequest(RelationshipRequest relationshipRequest);

    void denyFriendRequest(RelationshipRequest relationshipRequest);

    void deleteFriend(RelationshipRequest relationshipRequest);

    List<RelationshipResponse> findByUserIdAndStatus(String userId, RelationshipStatus status);

    List<RelationshipResponse> findByRelatedUserIdAndStatus(String userId, RelationshipStatus status);
}
