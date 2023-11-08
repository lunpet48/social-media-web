package com.webapp.socialmedia.controller.post;

import com.webapp.socialmedia.dto.WrappingResponse;
import com.webapp.socialmedia.dto.requests.PostRequest;
import com.webapp.socialmedia.dto.responses.PostResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.PostCannotUploadException;
import com.webapp.socialmedia.exceptions.PostNotFoundException;
import com.webapp.socialmedia.exceptions.UserNotAuthoritativeException;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.service.MediaService;
import com.webapp.socialmedia.service.PostMediaService;
import com.webapp.socialmedia.service.PostService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {
    private final PostMapper postMapper;
    private final PostService postService;
    private final MediaService mediaService;
    private final PostMediaService postMediaService;

    @PostMapping(value = "/post")
    @WrappingResponse(status = HttpStatus.CREATED)
    public Object createPost(@RequestPart PostRequest postRequest, @RequestPart MultipartFile[] files) {
        Post post = postService.createPost(postRequest);
        List<Media> mediaList = mediaService.uploadFiles(files, post);
        List<PostMedia> postMedia = postMediaService.uploadFiles(mediaList, post);
        return postMapper.toResponse(post, postMedia);
    }

    @PutMapping(value = "/post")
    @WrappingResponse(status = HttpStatus.OK)
    public Object updatePost(@RequestPart PostResponse postResponse, @RequestPart(required = false) MultipartFile[] filesToUpdate) throws PostCannotUploadException, PostNotFoundException {
        //Lấy current user
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pair<Post, List<PostMedia>> request = postMapper.toPostAndListPostMedia(postResponse);
        //Kiểm tra và cập nhật
        Post updatedPost = postService.updatePost(request.a, request.b, filesToUpdate, user.getId());
        List<Media> mediaList = mediaService.uploadFiles(filesToUpdate, updatedPost);
        Pair<List<String>, List<PostMedia>> files = postMediaService.updateFiles(request.b, mediaList, updatedPost);
        mediaService.deleteFiles(files.a);

        return postMapper.toResponse(updatedPost, Stream.concat(request.b.stream(), files.b.stream()).toList());
    }

    @DeleteMapping("/post/{postId}")
    @WrappingResponse(status = HttpStatus.OK)
    public Object deletePost(@PathVariable String postId) throws PostNotFoundException, UserNotAuthoritativeException {
//        String u = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        postService.deletePost(postId, user.getId());
        return null;
    }

    @GetMapping("/post/{postId}")
    @WrappingResponse(status = HttpStatus.OK)
    public Object getPost(@PathVariable String postId) throws PostNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postService.getPost(postId, user.getId());
        List<PostMedia> postMediaList = postMediaService.getFilesByPostId(postId);
        return postMapper.toResponse(post, postMediaList);
    }

    //    Lấy tất cả bài viết theo mã người dùng
    @GetMapping("/{userId}/posts")
    public ResponseEntity<ResponseDTO> getPostByUserId(@PathVariable String userId) {
        List<PostResponse> resultResponses = new ArrayList<>();
        List<Post> resultEntity = postService.getListPostByUserIdAndIsDeleted(userId);
        resultEntity.forEach(entity -> {
            resultResponses.add(postMapper.toResponse(entity, postMediaService.getFilesByPostId(entity.getId())));
        });
        return ResponseEntity.ok(ResponseDTO.builder().data(resultResponses).error(false).message("").build());
    }

    //Lấy các bài đăng của bạn bè trong 1 tháng gần nhất
    @GetMapping("/home")
    public ResponseEntity<ResponseDTO> getHomepage() {
        List<Post> resultEntity = postService.getHomepage();
        List<PostResponse> resultResponses = new ArrayList<>();
        resultEntity.forEach(entity -> {
            resultResponses.add(postMapper.toResponse(entity, postMediaService.getFilesByPostId(entity.getId())));
        });
        return ResponseEntity.ok(ResponseDTO.builder().data(resultResponses).error(false).message("").build());
    }
}
