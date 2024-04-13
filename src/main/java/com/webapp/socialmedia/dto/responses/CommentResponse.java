package com.webapp.socialmedia.dto.responses;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String id;
    private String postId;
    private String userId;
    private String comment;
    private String mediaLink;
    private Date createdAt;
    private String repliedCommentId;
}
