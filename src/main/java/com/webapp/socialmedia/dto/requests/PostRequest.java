package com.webapp.socialmedia.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String postType;
    private String postMode;
    private String caption;
    private List<String> tagList;
}
