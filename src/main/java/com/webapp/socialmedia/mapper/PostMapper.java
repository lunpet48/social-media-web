package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.AlbumResponse;
import com.webapp.socialmedia.dto.responses.PostResponse;
import com.webapp.socialmedia.dto.responses.ShortProfileResponse;
import com.webapp.socialmedia.entity.*;
import com.webapp.socialmedia.enums.PostMode;
import com.webapp.socialmedia.enums.PostType;
import com.webapp.socialmedia.repository.SavedPostRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    //    @Mapping(source = "post.user.id", target = "userId")
//    @Mapping(source = "post.id", target = "postId")
//    @Mapping(source = "post.caption", target = "caption")
//    @Mapping(source = "post.type", target = "postType")
//    @Mapping(source = "post.mode", target = "postMode")
//    @Mapping(source = "post.postTags", target = "tagList", qualifiedByName = "toListTag")
//    @Mapping(source = "media", target = "files", qualifiedByName = "toListFile")
    @Autowired
    private SavedPostRepository savedPostRepository;
    public PostResponse toResponse(Post post, List<PostMedia> media) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<SavedPost> savedPost = savedPostRepository.findById(SavedPostId.builder().postId(post.getId()).userId(user.getId()).build());
        PostResponse response = PostResponse.builder()
                .postType(post.getType().name())
                .postMode(post.getMode().name())
                .user(ShortProfileResponse.builder().userId(post.getUser().getId()).avatar(post.getUser().getProfile().getAvatar()).username(post.getUser().getUsername()).build())
                .postId(post.getId())
                .caption(post.getCaption())
                .tagList(new ArrayList<>())
                .files(new ArrayList<>())
                .reactions(post.getReactionList() == null || post.getReactionList().isEmpty() ? new ArrayList<>() : post.getReactionList().stream().map(x -> { return x.getUser().getId();}).toList())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .sharedPostId(post.getSharedPost() == null ? null : post.getSharedPost().getId())
                .saved(savedPost.isPresent())
                .album(AlbumResponse.builder().name(post.getAlbum().getName()).id(post.getAlbum().getId()).userId(post.getAlbum().getUser().getId()).build())
                .build();

        post.getPostTags().forEach(postTag -> {
            response.getTagList().add(postTag.getTag().getId());
        });

        media.forEach(m -> {
            response.getFiles().add(m.getMedia().getLink());
        });

        return response;
    }

    public PostResponse toResponse(Post post) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<SavedPost> savedPost = savedPostRepository.findById(SavedPostId.builder().postId(post.getId()).userId(user.getId()).build());
        PostResponse response = PostResponse.builder()
                .postType(post.getType().name())
                .postMode(post.getMode().name())
                .user(ShortProfileResponse.builder().userId(post.getUser().getId()).avatar(post.getUser().getProfile().getAvatar()).username(post.getUser().getUsername()).build())
                .postId(post.getId())
                .caption(post.getCaption())
                .tagList(new ArrayList<>())
                .files(new ArrayList<>())
                .reactions(post.getReactionList() == null || post.getReactionList().isEmpty() ? new ArrayList<>() : post.getReactionList().stream().map(x -> { return x.getUser().getId();}).toList())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .sharedPostId(post.getSharedPost().getId())
                .saved(savedPost.isPresent())
                .build();

        post.getPostTags().forEach(postTag -> {
            response.getTagList().add(postTag.getTag().getId());
        });

        return response;
    }
//
//    @Named("toListTag")
//    static List<String> toListTag(List<PostTag> tags) {
//        List<String> result = new ArrayList<>();
//        tags.forEach(tag -> {
//            result.add(tag.getTag().getId());
//        });
//        return result;
//    }
//
//    @Named("toListFile")
//    static List<String> toListFile(List<PostMedia> media) {
//        List<String> result = new ArrayList<>();
//        media.forEach(m -> {
//            result.add(m.getMediaId());
//        });
//        return result;
//    }

//    @Mapping(source = "postId", target = "a.id")
//    @Mapping(source = "userId", target = "a.user.id")
//    @Mapping(source = "postType", target = "a.type")
//    @Mapping(source = "postMode", target = "a.mode")
//    @Mapping(source = "caption", target = "a.caption")
////    @Mapping(source = "tagList", target = "a.tags", qualifiedByName = "toTag")
//    @Mapping(source = "files", target = "b", qualifiedByName = "toPostMedia")
//    Pair<Post, List<PostMedia>> toPostAndListPostMedia(PostResponse postResponse);

    public Pair<Post, List<PostMedia>> toPostAndListPostMedia(PostResponse postResponse) {
        Post post = Post.builder()
                .user(User.builder().id(postResponse.getUser().getUserId()).build())
                .id(postResponse.getPostId())
                .type(PostType.valueOf(postResponse.getPostType()))
                .mode(PostMode.valueOf(postResponse.getPostMode()))
                .caption(postResponse.getCaption())
                .postTags(new ArrayList<>())
                .build();

        List<PostMedia> postMediaList = new ArrayList<>();
        postResponse.getFiles().forEach(file -> {
            postMediaList.add(PostMedia.builder().media(Media.builder().link(file).build()).post(null).build());
        });
        if (postResponse.getTagList() != null) {
            postResponse.getTagList().forEach(tag -> {
                PostTagId postTagId = new PostTagId();
                postTagId.setTagId(tag.trim());
                postTagId.setPostId(post.getId());
                PostTag temp = PostTag.builder().id(postTagId).build();
                post.getPostTags().add(temp);
            });
        } else {
            postResponse.setTagList(new ArrayList<>());
        }

        return new Pair<>(post, postMediaList);
    }

//    @Mapping(source = "tagList", target = "tag", qualifiedByName = "toTag")
//    List<PostTag> toPostTagList(List<String> tagList, @Context Post post);
//
//    @Named("toTag")
//    static List<PostTag> toTag(List<String> tagList, @Context Post post) {
//        List<PostTag> result = new ArrayList<>();
//        tagList.forEach(tag -> {
//            result.add(PostTag.builder().tag(Tag.builder().id(tag.trim()).build()).post(post).id(PostTagId.builder().postId(post.getId()).tagId(tag.trim()).build()).build());
//        });
//        return result;
//    }
//
//    @Named("toPostMedia")
//    static List<PostMedia> toPostMedia(List<String> files) {
//        List<PostMedia> result = new ArrayList<>();
//        files.forEach(file -> {
//            result.add(PostMedia.builder().mediaId(file).media(null).post(null).build());
//        });
//        return result;
//    }
}
