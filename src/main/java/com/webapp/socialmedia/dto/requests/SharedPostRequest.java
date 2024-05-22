package com.webapp.socialmedia.dto.requests;

import com.webapp.socialmedia.enums.PostMode;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedPostRequest {
    private PostMode mode;
    private String caption;
    private String postId;
    private List<String> tagList;
}
