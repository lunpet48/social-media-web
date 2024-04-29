package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.entity.Reaction;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String postId;
    private ProfileResponseV2 user;
    private String postType;
    private String postMode;
    private String caption;
    private List<String> tagList;
    private List<String> files;
    private List<String> reactions;
    private Date createdAt;
    private Date updatedAt;
}
