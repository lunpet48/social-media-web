package com.webapp.socialmedia.dto.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavedPostResponse {
    private String postId;
    private String userId;
}
