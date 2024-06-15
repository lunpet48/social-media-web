package com.webapp.socialmedia.dto.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private String roomId;
    private String name;
    private List<ShortProfileResponse> users;
}
