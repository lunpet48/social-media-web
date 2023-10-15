package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.PostResponse;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.entity.Tag;
import org.antlr.v4.runtime.misc.Pair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "post.user.id", target = "userId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "post.caption", target = "caption")
    @Mapping(source = "post.type", target = "postType")
    @Mapping(source = "post.mode", target = "postMode")
    @Mapping(source = "post.tags", target = "tagList", qualifiedByName = "toListTag")
    @Mapping(source = "media", target = "files", qualifiedByName = "toListFile")
    PostResponse toResponse(Post post, List<PostMedia> media);

    @Named("toListTag")
    static Set<String> toListTag(Set<Tag> tags) {
        Set<String> result = new HashSet<>();
        tags.forEach(tag -> {
            result.add(tag.getId());
        });
        return result;
    }

    @Named("toListFile")
    static List<String> toListFile(List<PostMedia> media) {
        List<String> result = new ArrayList<>();
        media.forEach(m -> {
            result.add(m.getMediaId());
        });
        return result;
    }

    @Mapping(source = "postId", target = "a.id")
    @Mapping(source = "userId", target = "a.user.id")
    @Mapping(source = "postType", target = "a.type")
    @Mapping(source = "postMode", target = "a.mode")
    @Mapping(source = "caption", target = "a.caption")
    @Mapping(source = "tagList", target = "a.tags", qualifiedByName = "toTag")
    @Mapping(source = "files", target = "b", qualifiedByName = "toPostMedia")
    Pair<Post, List<PostMedia>> toPostAndListPostMedia(PostResponse postResponse);

    @Named("toTag")
    static Set<Tag> toTag(Set<String> tagList) {
        Set<Tag> result = new HashSet<>();
        tagList.forEach(tag -> {
            result.add(Tag.builder().id(tag).build());
        });
        return result;
    }

    @Named("toPostMedia")
    static List<PostMedia> toPostMedia(List<String> files) {
        List<PostMedia> result = new ArrayList<>();
        files.forEach(file -> {
            result.add(PostMedia.builder().mediaId(file).post(null).build());
        });
        return result;
    }
}
