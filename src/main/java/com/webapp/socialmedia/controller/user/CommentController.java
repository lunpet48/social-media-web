package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.requests.CommentRequest;
import com.webapp.socialmedia.dto.responses.CommentResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.service.CommentService;
import com.webapp.socialmedia.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final MediaService mediaService;
    @PostMapping("comment")
    public ResponseEntity<?> commentPost(@RequestPart CommentRequest commentRequest, @RequestPart MultipartFile file) throws IOException {
        Media media = mediaService.uploadFile(file, "COMMENT");
        CommentResponse commentResponse = commentService.createComment(commentRequest, media);
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
