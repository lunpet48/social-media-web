package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper( CommentMapper.class );

    @Mapping(source = "comment.post.id", target = "postId")
    @Mapping(source = "comment.user.id", target = "userId")
    @Mapping(source = "comment.repliedComment.id", target = "repliedCommentId")
    CommentResponse toResponse(Comment comment);
}
