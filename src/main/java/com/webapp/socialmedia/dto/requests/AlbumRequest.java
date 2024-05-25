package com.webapp.socialmedia.dto.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumRequest {
    private String id;
    private String name;
    private List<ShortPostRequest> posts;
}
