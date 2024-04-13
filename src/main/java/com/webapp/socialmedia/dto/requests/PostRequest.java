package com.webapp.socialmedia.dto.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String postType;
    private String postMode;
    private String caption;
    private List<String> tagList;
}
