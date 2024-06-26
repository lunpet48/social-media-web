package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.responses.PostResponseV2;
import com.webapp.socialmedia.dto.responses.SavedPostResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.*;
import com.webapp.socialmedia.enums.PostMode;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.PostNotFoundException;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.mapper.PostMapperImpl;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.repository.SavedPostRepository;
import com.webapp.socialmedia.service.PostService;
import com.webapp.socialmedia.service.SavedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavedPostServiceImpl implements SavedPostService {
    private final SavedPostRepository savedPostRepository;
    private final PostRepository postRepository;
    private final RelationshipRepository relationshipRepository;
    @Override
    public SavedPostResponse savePost(String postId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findByIdAndIsDeleted(postId, false).orElseThrow(PostNotFoundException::new);

        Relationship relationship = relationshipRepository.findByUserIdAndRelatedUserId(currentUser.getId(), post.getUser().getId()).orElse(null);

        if(post.getMode().equals(PostMode.PUBLIC) ||
                relationship != null && post.getMode().equals(PostMode.FRIEND) && relationship.getStatus().equals(RelationshipStatus.FRIEND) ||
                post.getUser().equals(currentUser)) {

            SavedPost savedPost = SavedPost.builder()
                .user(currentUser)
                .post(post)
                .id(SavedPostId.builder().postId(postId).userId(currentUser.getId()).build())
                .build();

            savedPostRepository.saveAndFlush(savedPost);
            return SavedPostResponse.builder()
                    .postId(savedPost.getPost().getId())
                    .userId(savedPost.getUser().getId())
                    .build();
        }

        throw new BadRequestException("Không tìm thấy bài viết");
    }

    @Override
    public void unsavedPost(String postId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SavedPost savedPost = savedPostRepository.findById(SavedPostId.builder().userId(user.getId()).postId(postId).build()).orElseThrow(() -> new BadRequestException("Không tìm thấy bài viết đã lưu"));
        savedPostRepository.delete(savedPost);
    }

    @Override
    public List<Post> getAllSavedPost() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //Lấy các bài đã lưu của bản thân và bài đăng chưa bị xóa sắp xếp theo thời gian
        List<SavedPost> savedPosts = savedPostRepository.findByUser_IdAndPost_IsDeletedOrderByCreatedAtDesc(user.getId(), false);
        List<Post> response = new ArrayList<>();
        for (SavedPost savedPost : savedPosts) {
            Optional<Relationship> relationship = relationshipRepository.findByUserIdAndRelatedUserId(user.getId(), savedPost.getPost().getUser().getId());
            //Lấy relationship và check xem có block/bị block hay chưa
            if (relationship.isPresent() && (relationship.get().getStatus().equals(RelationshipStatus.BLOCK) || relationship.get().getStatus().equals(RelationshipStatus.BLOCKED)))
                continue;
            //Nếu bài viết công khai hoặc bạn bè và có qh bạn bè hoặc bài viết của bản thân thì mới coi được
            if(savedPost.getPost().getMode().equals(PostMode.PUBLIC)) {
                response.add(savedPost.getPost());
            } else if (relationship.isPresent() && savedPost.getPost().getMode().equals(PostMode.FRIEND) && relationship.get().getStatus().equals(RelationshipStatus.FRIEND)) {
                response.add(savedPost.getPost());
            } else if (savedPost.getPost().getUser().equals(user)) {
                response.add(savedPost.getPost());
            }
        }
        return response;
    }

}
