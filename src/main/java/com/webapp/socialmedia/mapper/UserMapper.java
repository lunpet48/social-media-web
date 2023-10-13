package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.UserResponse;
import com.webapp.socialmedia.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    UserResponse userToUserResponse(User user);
}
