package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.RelationshipRequest;
import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.RelationshipId;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.RelationshipNotFoundException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.mapper.RelationshipMapper;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.IRelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelationshipServiceImpl implements IRelationshipService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    @Override
    public RelationshipResponse sendFriendRequest(RelationshipRequest relationshipRequest) {
        Relationship relationship = parseRelationshipRequest(relationshipRequest);
        relationship.setStatus(RelationshipStatus.PENDING);
        relationshipRepository.save(relationship);
        return RelationshipMapper.INSTANCE.RelationshipToRelationshipResponse(relationship);
    }

    @Override
    public void cancelFriendRequest(RelationshipRequest relationshipRequest) {
        RelationshipId id = new RelationshipId();
        id.setUser1Id(relationshipRequest.getUserId());
        id.setUser2Id(relationshipRequest.getUserTargetId());

        Relationship relationship = relationshipRepository.findByIdAndStatus(id, RelationshipStatus.PENDING)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));

        relationshipRepository.delete(relationship);
    }

    @Override
    public RelationshipResponse acceptFriendRequest(RelationshipRequest relationshipRequest) {
        RelationshipId id = new RelationshipId();
        id.setUser2Id(relationshipRequest.getUserId());
        id.setUser1Id(relationshipRequest.getUserTargetId());

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
        id.setUser2Id(relationshipRequest.getUserId());
        id.setUser1Id(relationshipRequest.getUserTargetId());

        Relationship relationship = relationshipRepository.findByIdAndStatus(id, RelationshipStatus.PENDING)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));

        relationshipRepository.delete(relationship);
    }

    @Override
    public void deleteFriend(RelationshipRequest relationshipRequest) {
        RelationshipId id1 = new RelationshipId();
        id1.setUser1Id(relationshipRequest.getUserId());
        id1.setUser2Id(relationshipRequest.getUserTargetId());

        Relationship relationship1 = relationshipRepository.findByIdAndStatus(id1, RelationshipStatus.FRIEND)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));

        RelationshipId id2 = new RelationshipId();
        id2.setUser2Id(relationshipRequest.getUserId());
        id2.setUser1Id(relationshipRequest.getUserTargetId());

        Relationship relationship2 = relationshipRepository.findByIdAndStatus(id2, RelationshipStatus.FRIEND)
                .orElseThrow(()-> new RelationshipNotFoundException("Failed"));

        relationshipRepository.delete(relationship1);
        relationshipRepository.delete(relationship2);
    }

    private Relationship parseRelationshipRequest(RelationshipRequest relationshipRequest){
        User user1 = userRepository.findById(relationshipRequest.getUserId()).orElseThrow(UserNotFoundException::new);
        User user2 = userRepository.findById(relationshipRequest.getUserTargetId()).orElseThrow(UserNotFoundException::new);
        RelationshipId id = new RelationshipId();
        id.setUser1Id(user1.getId());
        id.setUser2Id(user2.getId());
        Relationship relationship = new Relationship();
        relationship.setId(id);
        relationship.setUser1(user1);
        relationship.setUser2(user2);
        return relationship;
    }
}
