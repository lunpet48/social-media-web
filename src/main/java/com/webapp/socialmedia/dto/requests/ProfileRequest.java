package com.webapp.socialmedia.dto.requests;

import com.webapp.socialmedia.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    private String userId;
    private String bio;
    private String avatar;
    private String fullName;
}
