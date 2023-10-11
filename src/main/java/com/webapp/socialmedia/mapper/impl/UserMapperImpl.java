package com.webapp.socialmedia.mapper.impl;

import com.webapp.socialmedia.dto.responses.UserResponse;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserResponse userToUserResponse(User user) {
        if(user == null){
            return null;
        }
        UserResponse userResponse = UserResponse
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isLocked(user.getIsLocked())
                .lockTo(user.getLockTo())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        return userResponse;
    }
}
