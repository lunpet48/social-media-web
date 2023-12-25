package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.RelationshipId;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public abstract class RelationshipMapper {
    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "user", source ="relationship.user" )
    @Mapping(target = "userRelated", source ="relationship.relatedUser" )
    abstract public RelationshipResponse RelationshipToRelationshipResponse(Relationship relationship);

    public Relationship ToRelationship(String currentUserId, String targetUserId, RelationshipStatus status){
        User currentUser = userRepository.findById(currentUserId).orElseThrow(UserNotFoundException::new);
        User targetUser = userRepository.findById(targetUserId).orElseThrow(UserNotFoundException::new);
        RelationshipId id = RelationshipId.builder()
                .userId(currentUserId)
                .relatedUserId(targetUserId)
                .build();

        return Relationship.builder()
                .id(id)
                .user(currentUser)
                .relatedUser(targetUser)
                .status(status)
                .build();
    }
}
