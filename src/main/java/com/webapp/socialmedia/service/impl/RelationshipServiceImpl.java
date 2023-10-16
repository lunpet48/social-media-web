package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.RelationshipRequest;
import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.RelationshipId;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.RelationshipNotFoundException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.mapper.RelationshipMapper;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.IRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipServiceImpl implements IRelationshipService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    @Override
    public RelationshipResponse sendFriendRequest(RelationshipRequest relationshipRequest) {

        if(relationshipRepository.findByUserIdAndRelatedUserIdAndStatus(
                relationshipRequest.getUserId(),
                relationshipRequest.getUserRelatedId(),
                RelationshipStatus.FRIEND)
                .isPresent())
            throw new BadRequestException("Failed");

        Relationship relationship = parseRelationshipRequest(relationshipRequest);
        relationship.setStatus(RelationshipStatus.PENDING);
        relationshipRepository.save(relationship);
        return RelationshipMapper.INSTANCE.RelationshipToRelationshipResponse(relationship);
    }

    @Override
    public void cancelFriendRequest(RelationshipRequest relationshipRequest) {
        RelationshipId id = new RelationshipId();
        id.setUserId(relationshipRequest.getUserId());
        id.setRelatedUserId(relationshipRequest.getUserRelatedId());

        Relationship relationship = relationshipRepository.findByIdAndStatus(id, RelationshipStatus.PENDING)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));

        relationshipRepository.delete(relationship);
    }

    @Override
    public RelationshipResponse acceptFriendRequest(RelationshipRequest relationshipRequest) {
        RelationshipId id = new RelationshipId();
        id.setRelatedUserId(relationshipRequest.getUserId());
        id.setUserId(relationshipRequest.getUserRelatedId());

        Relationship relationship1 = relationshipRepository.findByIdAndStatus(id, RelationshipStatus.PENDING)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));
        relationship1.setStatus(RelationshipStatus.FRIEND);

        Relationship relationship2 = parseRelationshipRequest(relationshipRequest);
        relationship2.setStatus(RelationshipStatus.FRIEND);

        relationshipRepository.save(relationship1);
        relationshipRepository.save(relationship2);
        return RelationshipMapper.INSTANCE.RelationshipToRelationshipResponse(relationship2);
    }

    @Override
    public void denyFriendRequest(RelationshipRequest relationshipRequest) {
        RelationshipId id = new RelationshipId();
        id.setRelatedUserId(relationshipRequest.getUserId());
        id.setUserId(relationshipRequest.getUserRelatedId());

        Relationship relationship = relationshipRepository.findByIdAndStatus(id, RelationshipStatus.PENDING)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));

        relationshipRepository.delete(relationship);
    }

    @Override
    public void deleteFriend(RelationshipRequest relationshipRequest) {
        RelationshipId id1 = new RelationshipId();
        id1.setUserId(relationshipRequest.getUserId());
        id1.setRelatedUserId(relationshipRequest.getUserRelatedId());

        Relationship relationship1 = relationshipRepository.findByIdAndStatus(id1, RelationshipStatus.FRIEND)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));

        RelationshipId id2 = new RelationshipId();
        id2.setRelatedUserId(relationshipRequest.getUserId());
        id2.setUserId(relationshipRequest.getUserRelatedId());

        Relationship relationship2 = relationshipRepository.findByIdAndStatus(id2, RelationshipStatus.FRIEND)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));

        relationshipRepository.delete(relationship1);
        relationshipRepository.delete(relationship2);
    }

    @Override
    public List<RelationshipResponse> findByUserIdAndStatus(String userId,RelationshipStatus status){
        List<Relationship> relationships = relationshipRepository.findByUserIdAndStatus(userId,status);
        List<RelationshipResponse> relationshipResponses = new ArrayList<>();
        for (Relationship relationship: relationships) {
            RelationshipResponse relationshipResponse = RelationshipMapper.INSTANCE.RelationshipToRelationshipResponse(relationship);
            relationshipResponses.add(relationshipResponse);
        }
        return relationshipResponses;
    }

    @Override
    public List<RelationshipResponse> findByRelatedUserIdAndStatus(String userId, RelationshipStatus status) {
        List<Relationship> relationships = relationshipRepository.findByRelatedUserIdAndStatus(userId,status);
        List<RelationshipResponse> relationshipResponses = new ArrayList<>();
        for (Relationship relationship: relationships) {
            RelationshipResponse relationshipResponse = RelationshipMapper.INSTANCE.RelationshipToRelationshipResponse(relationship);
            relationshipResponses.add(relationshipResponse);
        }
        return relationshipResponses;
    }

    private Relationship parseRelationshipRequest(RelationshipRequest relationshipRequest){
        User user1 = userRepository.findById(relationshipRequest.getUserId()).orElseThrow(UserNotFoundException::new);
        User user2 = userRepository.findById(relationshipRequest.getUserRelatedId()).orElseThrow(UserNotFoundException::new);
        RelationshipId id = new RelationshipId();
        id.setUserId(user1.getId());
        id.setRelatedUserId(user2.getId());
        Relationship relationship = new Relationship();
        relationship.setId(id);
        relationship.setUser(user1);
        relationship.setRelatedUser(user2);
        return relationship;
    }
}
