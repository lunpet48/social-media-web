package com.webapp.socialmedia.dto.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponseV2 {
    private String roomId;
    private List<ProfileResponseV2> users;
    private MessageResponse message;
}
