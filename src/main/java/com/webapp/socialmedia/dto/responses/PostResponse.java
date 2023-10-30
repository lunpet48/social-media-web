package com.webapp.socialmedia.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String postId;
    private String userId;
    private String postType;
    private String postMode;
    private String caption;
    private List<String> tagList;
    private List<String> files;
}
