package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hashtag")
@RequiredArgsConstructor
public class HashtagController {
    private final HashtagService hashtagService;
    @GetMapping("/{name}/posts")
    public ResponseEntity<?> getPostsByHashtag(@PathVariable String name){
        return ResponseEntity.ok(new ResponseDTO().success(hashtagService.getPostsByHashtag(name)));
    }
}
