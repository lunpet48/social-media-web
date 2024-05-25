package com.webapp.socialmedia.controller.post;

import com.webapp.socialmedia.dto.requests.AlbumRequest;
import com.webapp.socialmedia.dto.responses.PostResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.service.AlbumService;
import com.webapp.socialmedia.service.PostMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AlbumController {
    private final AlbumService albumService;
    private final PostMediaService postMediaService;
    private final PostMapper postMapper;
    @PostMapping("/album")
    //Tạo album hoặc thêm bài đăng vào album có sẵn
    public ResponseEntity<?> createAlbum(@RequestBody AlbumRequest albumRequest) {
        return ResponseEntity.ok(albumService.createAlbum(albumRequest));
    }

    @GetMapping("/album")
    //Lấy các album hiện có của người dùng
    public ResponseEntity<?> getAllMyAlbum() {
        return ResponseEntity.ok(new ResponseDTO().success(albumService.getAllMyAlbums()));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<?> getPostsInAlbum(@PathVariable String albumId) {
        List<PostResponse> postResponseList = new ArrayList<>();
        List<Post> posts = albumService.getPostsInAlbum(albumId);
        posts.forEach(post -> {
            postResponseList.add(postMapper.toResponse(post, postMediaService.getFilesByPostId(post.getId())));
        });
        return ResponseEntity.ok(new ResponseDTO().success(postResponseList));
    }
}
