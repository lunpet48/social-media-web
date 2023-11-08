package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.entity.Relationship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RelationshipMapper {
    RelationshipMapper INSTANCE = Mappers.getMapper( RelationshipMapper.class );

    @Mapping(target = "user", expression = "java(UserMapper.INSTANCE.userToUserProfileResponse(relationship.getUser()))")
    @Mapping(target = "userRelated", expression = "java(UserMapper.INSTANCE.userToUserProfileResponse(relationship.getRelatedUser()))")
    RelationshipResponse RelationshipToRelationshipResponse(Relationship relationship);
}
