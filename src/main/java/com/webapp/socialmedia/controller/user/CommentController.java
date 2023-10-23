package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.requests.CommentRequest;
import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("comment")
    public ResponseEntity<?> commentPost(@RequestBody CommentRequest commentRequest){
        CommentResponse commentResponse = commentService.createComment(commentRequest);
        return ResponseEntity.ok(new ResponseDTO().success(commentResponse));
    }

    @DeleteMapping("comment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@RequestParam String commentId){
        commentService.deleteComment(commentId);
    }

    @GetMapping("posts/{postId}/comments")
    public ResponseEntity<?> getCommentOfPost(@PathVariable String postId){
        List<CommentResponse> commentResponses = commentService.getComment(postId);
        return ResponseEntity.ok(new ResponseDTO().success(commentResponses));
    }
}
