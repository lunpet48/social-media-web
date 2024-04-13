package com.webapp.socialmedia.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    @JsonIgnore
    private String refreshToken;
    private UserProfileResponse user;
}
