package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.dto.responses.ProfileResponseV2;
import com.webapp.socialmedia.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

//    @Mapping(source = "comment.post.id", target = "postId")
//    @Mapping(source = "comment.user.id", target = "userId")
//    @Mapping(source = "comment.repliedComment.id", target = "repliedCommentId")
//    @Mapping(source = "comment.media.link", target = "mediaLink")
    public CommentResponse toResponse(Comment comment) {
        CommentResponse commentResponse = CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .user(ProfileResponseV2.builder().userId(comment.getUser().getId()).username(comment.getUser().getUsername()).avatar(comment.getUser().getProfile().getAvatar()).build())
                .repliedCommentId(comment.getRepliedComment() == null ? null : comment.getRepliedComment().getId())
                .createdAt(comment.getCreatedAt())
                .mediaLink(comment.getMedia() == null ? null : comment.getMedia().getLink())
                .postId(comment.getPost().getId())
                .build();
        return commentResponse;
    }
}
