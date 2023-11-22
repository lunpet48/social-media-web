package com.webapp.socialmedia.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseV2 {
    private String postId;
//    private String userId;
    private String postType;
    private String postMode;
    private String caption;
    private List<String> tagList;
    private List<String> files;
    private List<String> reactions;
    private Date createdAt;
    private UserProfileResponse user;
}
