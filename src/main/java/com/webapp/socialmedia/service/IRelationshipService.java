package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.RelationshipRequest;
import com.webapp.socialmedia.dto.responses.RelationshipResponse;

public interface IRelationshipService {
    RelationshipResponse sendFriendRequest(RelationshipRequest relationshipRequest);

    void cancelFriendRequest(RelationshipRequest relationshipRequest);

    RelationshipResponse acceptFriendRequest(RelationshipRequest relationshipRequest);

    void denyFriendRequest(RelationshipRequest relationshipRequest);

    void deleteFriend(RelationshipRequest relationshipRequest);
}
