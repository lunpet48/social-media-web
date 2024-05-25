package com.webapp.socialmedia.dto.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumResponse {
    private String id;
    private String name;
    private String userId;

}
