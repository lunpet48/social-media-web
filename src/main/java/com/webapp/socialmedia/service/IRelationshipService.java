package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.RelationshipRequest;
import com.webapp.socialmedia.dto.responses.ShortProfileResponse;
import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.enums.RelationshipStatus;

import java.util.List;

public interface IRelationshipService {
    RelationshipResponse sendFriendRequest(RelationshipRequest relationshipRequest, String userId);

    void cancelFriendRequest(RelationshipRequest relationshipRequest, String userId);

    RelationshipResponse acceptFriendRequest(RelationshipRequest relationshipRequest, String userId);

    void denyFriendRequest(RelationshipRequest relationshipRequest, String userId);

    void deleteFriend(RelationshipRequest relationshipRequest, String userId);

    List<UserProfileResponse> findByUserIdAndStatus(String userId, RelationshipStatus status);

    List<UserProfileResponse> getFriends(String userId);

    List<UserProfileResponse> findByRelatedUserIdAndStatus(String userId, RelationshipStatus status);

    RelationshipResponse blockUser(RelationshipRequest relationshipRequest, String userId);

    void unblockUser(RelationshipRequest relationshipRequest, String userId);

    List<ShortProfileResponse> getOnlineUser();
}
