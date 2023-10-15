package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.RelationshipResponse;
import com.webapp.socialmedia.entity.Relationship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RelationshipMapper {
    RelationshipMapper INSTANCE = Mappers.getMapper( RelationshipMapper.class );

    @Mapping(target = "userId", expression = "java(relationship.getUser1().getId())")
    @Mapping(target = "userTargetId", expression = "java(relationship.getUser2().getId())")
    RelationshipResponse RelationshipToRelationshipResponse(Relationship relationship);
}
