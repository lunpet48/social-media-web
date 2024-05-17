package com.webapp.socialmedia.dto.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortProfileResponse {
    private String userId;
    private String username;
    private String avatar;
}
