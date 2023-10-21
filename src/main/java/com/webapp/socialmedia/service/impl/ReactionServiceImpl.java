package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.responses.ReactionResponse;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.Reaction;
import com.webapp.socialmedia.entity.ReactionId;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.ReactionRepository;
import com.webapp.socialmedia.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    @Override
    public ReactionResponse likePost(String postId, User user){
        // thiếu kiểm tra user có quyền xem bài viết không

        // Kiểm tra bài viết có tồn tại không
        Post post = postRepository.findByIdAndIsDeleted(postId, false)
                .orElseThrow(() ->new BadRequestException("Post Not Found"));

        //Kiểm tra đã like hay chưa
        Optional<Reaction> reactionExist = reactionRepository.findByPostIdAndUserId(post.getId(), user.getId());
        if(reactionExist.isPresent())
            throw new BadRequestException("You already like this post");

        ReactionId id = new ReactionId();
        id.setUserId(user.getId());
        id.setPostId(post.getId());

        Reaction reaction = Reaction.builder()
                .id(id)
                .user(user)
                .post(post)
                .build();
        reactionRepository.save(reaction);
        return ReactionResponse.builder()
                .userId(user.getId())
                .postId(post.getId())
                .liked(true)
                .build();
    }

    @Override
    public ReactionResponse dislikePost(String postId, User user){
        // thiếu kiểm tra user có quyền xem bài viết không

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->new BadRequestException("Post Not Found"));

        Reaction reaction = reactionRepository.findByPostIdAndUserId(post.getId(), user.getId())
                .orElseThrow(()-> new BadRequestException("Not Found"));

        reactionRepository.delete(reaction);

        return ReactionResponse.builder()
                .userId(user.getId())
                .postId(post.getId())
                .liked(false)
                .build();
    }
}
