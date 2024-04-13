package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.enums.Role;
import lombok.*;

import java.util.Date;

@Getter
@Setter
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
