package com.webapp.socialmedia.dto.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    private String roomId;
    private List<ShortProfileResponse> users;
    private List<MessageResponse> message;
    private boolean isRead;
}
