package com.webapp.socialmedia.dto.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRequest {
    private List<UserRequest> userList;
    private String roomId;
}
