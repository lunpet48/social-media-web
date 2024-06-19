package com.webapp.socialmedia.controller.report;

import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.service.AdminService;
import com.webapp.socialmedia.service.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final PostMediaService postMediaService;
    private final PostMapper postMapper;

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPost(@PathVariable String postId) {
        Post post = adminService.findPostById(postId);
        return ResponseEntity.ok(new ResponseDTO().success(postMapper.toResponse(post, postMediaService.getFilesByPostId(postId))));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(new ResponseDTO().success(adminService.findUserById(userId)));
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable String commentId) {
        return ResponseEntity.ok(new ResponseDTO().success(adminService.findCommentById(commentId)));
    }
}
