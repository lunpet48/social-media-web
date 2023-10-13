package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse{
    private String id;

    private String username;

    private String email;

    private Role role = Role.USER;

    private Boolean isLocked = false;

    private Date lockTo;

    private Date createdAt;

    private Date updatedAt;

    private ProfileResponse profile;
}
