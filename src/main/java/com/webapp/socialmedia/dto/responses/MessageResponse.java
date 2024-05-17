package com.webapp.socialmedia.dto.responses;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String messageId;
    private ShortProfileResponse sender;
    private String roomId;
    private String message;
    private String mediaLink;
    private Date createdAt;
}
