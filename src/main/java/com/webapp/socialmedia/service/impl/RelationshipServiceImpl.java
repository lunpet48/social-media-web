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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationshipServiceImpl implements IRelationshipService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    @Override
    public RelationshipResponse sendFriendRequest(RelationshipRequest relationshipRequest, String userId) {
        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserId(
                        relationshipRequest.getTargetId(),
                        userId
                );
        if(relationshipOptional.isPresent()){
            Relationship relationshipCheck = relationshipOptional.get();
            if(relationshipCheck.getStatus().equals(RelationshipStatus.PENDING)){
                throw new BadRequestException("Đối phương đã gửi lời mời đến bạn");
            }
            if(relationshipCheck.getStatus().equals(RelationshipStatus.FRIEND)){
                throw new BadRequestException("Các bạn đã là bạn bè");
            }
        }

        Relationship newRelationship = parseRelationshipRequest(relationshipRequest, userId);
        newRelationship.setStatus(RelationshipStatus.PENDING);
        relationshipRepository.save(newRelationship);
        return RelationshipMapper.INSTANCE.RelationshipToRelationshipResponse(newRelationship);
    }

    @Override
    public void cancelFriendRequest(RelationshipRequest relationshipRequest, String userId) {

        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        userId,
                        relationshipRequest.getTargetId(),
                        RelationshipStatus.PENDING
                );

        if(relationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");

        Relationship relationship = relationshipOptional.get();
        relationshipRepository.delete(relationship);
    }

    @Override
    public RelationshipResponse acceptFriendRequest(RelationshipRequest relationshipRequest, String userId) {

        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        relationshipRequest.getTargetId(),
                        userId,
                        RelationshipStatus.PENDING
                );

        if(relationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");


        Relationship relationship = parseRelationshipRequest(relationshipRequest, userId);
        relationship.setStatus(RelationshipStatus.FRIEND);

        Relationship reverseRelationship = relationshipOptional.get();
        reverseRelationship.setStatus(RelationshipStatus.FRIEND);

        relationshipRepository.save(reverseRelationship);
        relationshipRepository.save(relationship);
        return RelationshipMapper.INSTANCE.RelationshipToRelationshipResponse(relationship);
    }

    @Override
    public void denyFriendRequest(RelationshipRequest relationshipRequest, String userId) {

        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        relationshipRequest.getTargetId(),
                        userId,
                        RelationshipStatus.PENDING
                );

        if(relationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");

        Relationship relationship = relationshipOptional.get();

        relationshipRepository.delete(relationship);
    }

    @Override
    public void deleteFriend(RelationshipRequest relationshipRequest, String userId) {

        Optional<Relationship> relationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        userId,
                        relationshipRequest.getTargetId(),
                        RelationshipStatus.FRIEND
                );
        if(relationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");
        Relationship relationship = relationshipOptional.get();

        Optional<Relationship> reverseRelationshipOptional = relationshipRepository
                .findByUserIdAndRelatedUserIdAndStatus(
                        relationshipRequest.getTargetId(),
                        userId,
                        RelationshipStatus.FRIEND
                );
        if(reverseRelationshipOptional.isEmpty())
            throw new RelationshipNotFoundException("Không tìm thấy yêu cầu");
        Relationship reverseRelationship = reverseRelationshipOptional.get();

        relationshipRepository.delete(relationship);
        relationshipRepository.delete(reverseRelationship);
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

    @Override
    public RelationshipResponse blockUser(RelationshipRequest relationshipRequest, String userId) {
        try {
            deleteFriend(relationshipRequest, userId);
        }
        catch (Exception ignored){}

        Relationship newRelationship = parseRelationshipRequest(relationshipRequest, userId);
        newRelationship.setStatus(RelationshipStatus.BLOCK);
        relationshipRepository.save(newRelationship);
        return RelationshipMapper.INSTANCE.RelationshipToRelationshipResponse(newRelationship);
    }

    @Override
    public void unblockUser(RelationshipRequest relationshipRequest, String userId) {
        Relationship relationship = relationshipRepository.findByUserIdAndRelatedUserIdAndStatus(
                userId,
                relationshipRequest.getTargetId(),
                RelationshipStatus.BLOCK
        ).orElseThrow(()-> new RelationshipNotFoundException("Không tìm thấy yêu cầu"));
        relationshipRepository.delete(relationship);
    }

    private Relationship parseRelationshipRequest(RelationshipRequest relationshipRequest, String userId){
        User user1 = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User user2 = userRepository.findById(relationshipRequest.getTargetId()).orElseThrow(UserNotFoundException::new);
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
