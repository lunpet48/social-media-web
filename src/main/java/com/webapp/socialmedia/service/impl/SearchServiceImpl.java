package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.responses.PostResponseV2;
import com.webapp.socialmedia.dto.responses.SearchResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipProfile;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.PostMediaService;
import com.webapp.socialmedia.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMediaService postMediaService;
    private final RelationshipRepository relationshipRepository;
    @Override
    public SearchResponse search(String keyword){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<User> users = userRepository.searchUser(keyword);
        List<UserProfileResponse> userProfileResponses = new ArrayList<>();
        users.forEach(user -> {
            if(!user.getId().equals(currentUser.getId())) {
                UserProfileResponse userProfileResponse = UserMapper.INSTANCE.userToUserProfileResponse(user);

                if(relationshipRepository.findByUserIdAndRelatedUserIdAndStatus(user.getId(),currentUser.getId(), RelationshipStatus.PENDING).isPresent()){
                    userProfileResponse.setRelationship(RelationshipProfile.INCOMMINGREQUEST);
                }
                else {
                    Optional<Relationship> relationshipOptional = relationshipRepository.findByUserIdAndRelatedUserId(currentUser.getId(), user.getId());
                    if(relationshipOptional.isEmpty()){
                        userProfileResponse.setRelationship(RelationshipProfile.STRANGER);
                    }
                    else {
                        Relationship relationship = relationshipOptional.get();
                        userProfileResponse.setRelationship(RelationshipProfile.valueOf(relationship.getStatus().name()));
                    }
                }

                userProfileResponses.add(userProfileResponse);
            }
        });

        List<PostResponseV2> postResponses = new ArrayList<>();
        List<Post> posts = postRepository.searchPost(keyword);

        posts.forEach(post -> {
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
                    .user(UserMapper.INSTANCE.userToUserProfileResponse(post.getUser()))
                    .build();

            post.getPostTags().forEach(postTag -> response.getTagList().add(postTag.getTag().getId()));

            media.forEach(m -> response.getFiles().add(m.getMedia().getLink()));

            postResponses.add(response);
        });

        return SearchResponse.builder()
                .users(userProfileResponses)
                .posts(postResponses)
                .build();
    }
}
