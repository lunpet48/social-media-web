package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String userId;
    private String bio;
    private String avatar;
    private String fullName;
}
