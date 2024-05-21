package com.webapp.socialmedia.controller.post;

import com.webapp.socialmedia.dto.responses.PostResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.service.PostMediaService;
import com.webapp.socialmedia.service.SavedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SavedPostController {
    private final SavedPostService savedPostService;
    private final PostMediaService postMediaService;
    private final PostMapper postMapper;

    @PostMapping("/save/{postId}")
    public ResponseEntity<?> savePost(@PathVariable String postId) {
        return ResponseEntity.ok(new ResponseDTO().success(savedPostService.savePost(postId)));
    }

    @DeleteMapping("/save/{postId}")
    public ResponseEntity<?> deleteSavedPost(@PathVariable String postId) {
        savedPostService.unsavedPost(postId);
        return ResponseEntity.ok(new ResponseDTO());
    }

    @GetMapping("/saved/all-posts")
    public ResponseEntity<?> getAllSavedPost() {
        List<PostResponse> responses = new ArrayList<>();
        List<Post> postList = savedPostService.getAllSavedPost();
        postList.forEach(post -> {
            responses.add(postMapper.toResponse(post, postMediaService.getFilesByPostId(post.getId())));
        });
        return ResponseEntity.ok(new ResponseDTO().success(responses));
    }
}
