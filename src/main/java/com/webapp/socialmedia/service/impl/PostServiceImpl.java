package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.PostRequest;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.Tag;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.PostMode;
import com.webapp.socialmedia.enums.PostType;
import com.webapp.socialmedia.exceptions.PostNotFoundException;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.TagRepository;
import com.webapp.socialmedia.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

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
    public Post updatePost(Post post) throws PostNotFoundException {
        Post oldPost = postRepository.findById(post.getId()).orElseThrow(() -> new PostNotFoundException("Bài đăng không tồn tại"));
        oldPost.setType(post.getType());
        oldPost.setMode(post.getMode());
        oldPost.setCaption(post.getCaption());

        Set<Tag> tags = new HashSet<>();
        post.getTags().forEach(tag -> {
            if(tagRepository.findById(tag.getId().toLowerCase()).isEmpty())
                tags.add(tagRepository.saveAndFlush(Tag.builder().id(tag.getId().toLowerCase()).build()));
            tags.add(tag);
        });
        oldPost.setTags(tags);

        return postRepository.save(oldPost);
    }
}
