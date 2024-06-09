package com.webapp.socialmedia.dto.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumShortRequest {
    private String id;
    private String name;
}
