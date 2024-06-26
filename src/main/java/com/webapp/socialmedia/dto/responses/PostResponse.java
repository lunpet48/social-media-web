package com.webapp.socialmedia.dto.responses;

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
    private ShortProfileResponse user;
    private String postType;
    private String postMode;
    private String caption;
    private List<String> tagList;
    private List<String> files;
    private List<String> reactions;
    private Boolean saved;
    private String sharedPostId;
    private AlbumResponse album;
    private Date createdAt;
    private Date updatedAt;
}
