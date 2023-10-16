package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.PostRequest;
import com.webapp.socialmedia.entity.*;
import com.webapp.socialmedia.enums.PostMode;
import com.webapp.socialmedia.enums.PostType;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.PostCannotUploadException;
import com.webapp.socialmedia.exceptions.PostNotFoundException;
import com.webapp.socialmedia.exceptions.UserNotAuthoritativeException;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.repository.TagRepository;
import com.webapp.socialmedia.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final RelationshipRepository relationshipRepository;

    @Override
    public Post createPost(PostRequest postRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<Tag> tagResult = new HashSet<>();
        postRequest.getTagList().forEach(tag -> {
            if (tagRepository.findById(tag.toLowerCase()).isEmpty())
                tagResult.add(tagRepository.save(Tag.builder().id(tag.toLowerCase()).build()));
            else
                tagResult.add(Tag.builder().id(tag.toLowerCase()).build());
        });

        Post post = Post.builder().user(user)
                .mode(PostMode.valueOf(postRequest.getPostMode()))
                .type(PostType.valueOf(postRequest.getPostType()))
                .caption(postRequest.getCaption())
                .tags(tagResult).build();
        postRepository.save(post);

        return post;
    }

    @Override
    public Post updatePost(Post post, List<PostMedia> postMediaList, MultipartFile[] files, String userId) throws PostNotFoundException, PostCannotUploadException {
        Post oldPost = postRepository.findById(post.getId()).orElseThrow(() -> new PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn"));
        if (!oldPost.getUser().getId().equals(userId))
            throw new RuntimeException("Người dùng không có quyền");
        if (postMediaList.isEmpty() && files == null)
            throw new PostCannotUploadException("Không thể đăng/chỉnh sửa bài viết nếu thiếu hình ảnh hoặc video!!!");
        oldPost.setType(post.getType());
        oldPost.setMode(post.getMode());
        oldPost.setCaption(post.getCaption());

        Set<Tag> tags = new HashSet<>();
        post.getTags().forEach(tag -> {
            if (tagRepository.findById(tag.getId().toLowerCase()).isEmpty())
                tags.add(tagRepository.saveAndFlush(Tag.builder().id(tag.getId().toLowerCase()).build()));
            tags.add(tag);
        });
        oldPost.setTags(tags);

        return postRepository.save(oldPost);
    }

    @Override
    public void deletePost(String postId, String userId) throws PostNotFoundException, UserNotAuthoritativeException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn"));
        if (!post.getUser().getId().equals(userId))
            throw new UserNotAuthoritativeException("Người dùng không có quyền hạn này");

        post.setIsDeleted(true);
        postRepository.save(post);
    }

    @Override
    public Post getPost(String postId, String userId) throws PostNotFoundException {
        Post post = postRepository.findByIdAndIsDeleted(postId, false).orElseThrow(() -> new PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn"));

        PostMode mode = post.getMode();
        if (post.getUser().getId().equals(userId)) return post;
        if(mode.equals(PostMode.PUBLIC)) {
            return post;
        }
        if(mode.equals(PostMode.FRIEND)){
            Relationship relationship = relationshipRepository.findByUser1IdAndUser2Id(post.getUser().getId(), userId).orElseThrow(() -> new PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn"));
            if(relationship.getStatus().equals(RelationshipStatus.FRIEND))
                return post;
        }
        throw new  PostNotFoundException("Bài đăng không tồn tại hoặc đã bị ẩn");
    }
}
