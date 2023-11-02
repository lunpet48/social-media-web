package com.webapp.socialmedia.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String id;

    private String username;

    private String email;

    private Boolean isLocked;

    private String bio;

    private String avatar;

    private String fullName;

    private Integer postCount;

    private Integer friendCount;
}
