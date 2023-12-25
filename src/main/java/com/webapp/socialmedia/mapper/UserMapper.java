package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipProfile;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.repository.RelationshipRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = RelationshipRepository.class)
public abstract class UserMapper {
    @Autowired
    protected RelationshipRepository relationshipRepository;

    @Mapping(target = ".", source = "profile")
    @Mapping(target = "postCount", expression = "java(user.getPosts().size())")
    @Mapping(target = "friendCount",expression = "java(getFriendCount(user))")
    @Mapping(target = "relationship", ignore = true)
    abstract public UserProfileResponse userToUserProfileResponse(User user);

    @Mapping(target = ".", source = "user.profile")
    @Mapping(target = "postCount", expression = "java(user.getPosts().size())")
    @Mapping(target = "friendCount",expression = "java(getFriendCount(user))")
    @Mapping(target = "relationship", expression = "java(getRelationship(user, currentUserId))")
    abstract public UserProfileResponse userToUserProfileResponse(User user, String currentUserId);

    protected Integer getFriendCount(User user){
        return relationshipRepository.findByUserIdAndStatus(user.getId(), RelationshipStatus.FRIEND).size();
    }

    protected RelationshipProfile getRelationship(User user, String currentUserId){
        if(user.getId().equals(currentUserId)){
            return RelationshipProfile.SELF;
        }

        if (relationshipRepository.findByUserIdAndRelatedUserIdAndStatus(
                user.getId(),
                currentUserId,
                RelationshipStatus.PENDING
            ).isPresent()) {
            return RelationshipProfile.INCOMMINGREQUEST;
        }

        Optional<Relationship> relationshipOptional =
                relationshipRepository.findByUserIdAndRelatedUserId(
                        currentUserId,
                        user.getId()
                );
        if(relationshipOptional.isEmpty()){
            return RelationshipProfile.STRANGER;
        }
        Relationship relationship = relationshipOptional.get();
        return RelationshipProfile.valueOf(relationship.getStatus().name());
    }

//    void updateUserPassword(String password, @MappingTarget User user);
}
