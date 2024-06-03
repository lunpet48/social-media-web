package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.responses.PostResponse;
import com.webapp.socialmedia.dto.responses.ReactionResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.service.PostMediaService;
import com.webapp.socialmedia.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;
    private final PostMediaService postMediaService;
    private final PostMapper postMapper;
    @PostMapping("{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable String postId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReactionResponse reactionResponse = reactionService.likePost(postId, user);
        return ResponseEntity.ok(new ResponseDTO().success(reactionResponse));
    }

    @DeleteMapping("{postId}/like")
    public ResponseEntity<?> dislikePost(@PathVariable String postId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReactionResponse reactionResponse = reactionService.dislikePost(postId, user);
        return ResponseEntity.ok(new ResponseDTO().success(reactionResponse));
    }

    @GetMapping("{postId}/like")
    public ResponseEntity<?> getReaction(@PathVariable String postId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReactionResponse reactionResponse = reactionService.getReaction(postId, user);
        return ResponseEntity.ok(new ResponseDTO().success(reactionResponse));
    }

    @GetMapping("/liked")
    public ResponseEntity<?> getLikedPosts(@RequestParam int pageSize, @RequestParam int pageNo){
        List<Post> posts = reactionService.getLikedPosts(pageSize, pageNo);
        List<PostResponse> response = new ArrayList<>();

        posts.forEach(post -> {
            response.add(postMapper.toResponse(post, postMediaService.getFilesByPostId(post.getId())));
        });

        return ResponseEntity.ok(new ResponseDTO().success(response));
    }
}
