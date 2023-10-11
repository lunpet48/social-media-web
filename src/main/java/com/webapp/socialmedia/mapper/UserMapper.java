package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.UserResponse;
import com.webapp.socialmedia.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserResponse userToUserResponse(User user);
}
