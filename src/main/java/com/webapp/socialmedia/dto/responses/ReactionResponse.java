package com.webapp.socialmedia.dto.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReactionResponse {
    String userId;
    String postId;
    boolean liked;
}
