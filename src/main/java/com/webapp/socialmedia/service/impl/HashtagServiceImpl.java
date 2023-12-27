package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.responses.PostResponseV2;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.entity.PostTag;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.PostTagRepository;
import com.webapp.socialmedia.service.HashtagService;
import com.webapp.socialmedia.service.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    private final PostTagRepository postTagRepository;
    private final PostMediaService postMediaService;
    private final UserMapper userMapper;
    @Override
    public List<PostResponseV2> getPostsByHashtag(String name){
        List<PostTag> postTags = postTagRepository.findByTagName(name);

        List<PostResponseV2> postResponses = new ArrayList<>();
        for (PostTag postTag:postTags) {
            Post post = postTag.getPost();
            List<PostMedia> media = postMediaService.getFilesByPostId(post.getId());
            PostResponseV2 response = PostResponseV2.builder()
                    .postType(post.getType().name())
                    .postMode(post.getMode().name())
                    .postId(post.getId())
                    .caption(post.getCaption())
                    .tagList(new ArrayList<>())
                    .files(new ArrayList<>())
                    .reactions(post.getReactionList() == null || post.getReactionList().isEmpty() ? new ArrayList<>() : post.getReactionList().stream().map(x -> x.getUser().getId()).toList())
                    .createdAt(post.getCreatedAt())
                    .user(userMapper.userToUserProfileResponse(post.getUser()))
                    .build();

            post.getPostTags().forEach(pT -> response.getTagList().add(pT.getTag().getId()));

            media.forEach(m -> response.getFiles().add(m.getMedia().getLink()));

            postResponses.add(response);
        }
        return postResponses;
    }
}
