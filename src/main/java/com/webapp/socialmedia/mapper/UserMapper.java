package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.UserResponse;
import com.webapp.socialmedia.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    @Mapping(target = "profile", expression = "java(ProfileMapper.INSTANCE.ProfileToProfileResponse(user.getProfile()))")
    UserResponse userToUserResponse(User user);
}
