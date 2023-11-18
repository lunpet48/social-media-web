package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.UserProfileResponse;
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
    @Mapping(target = "postCount", expression = "java(user.getPosts().size())")
    @Mapping(target = "bio", source = "user.profile.bio")
    @Mapping(target = "avatar", source = "user.profile.avatar")
    @Mapping(target = "fullName", source = "user.profile.fullName")
    @Mapping(target = "gender", source = "user.profile.gender")
    @Mapping(target = "address", source = "user.profile.address")
    @Mapping(target = "dateOfBirth", source = "user.profile.dateOfBirth")
    UserProfileResponse userToUserProfileResponse(User user);
}
