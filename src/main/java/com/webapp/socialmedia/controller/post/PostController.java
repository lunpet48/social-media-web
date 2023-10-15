package com.webapp.socialmedia.controller.post;

import com.webapp.socialmedia.dto.WrappingResponse;
import com.webapp.socialmedia.dto.requests.PostRequest;
import com.webapp.socialmedia.dto.responses.PostResponse;
import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.PostCannotUploadException;
import com.webapp.socialmedia.exceptions.PostNotFoundException;
import com.webapp.socialmedia.mapper.PostMapper;
import com.webapp.socialmedia.service.MediaService;
import com.webapp.socialmedia.service.PostMediaService;
import com.webapp.socialmedia.service.PostService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;
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
    public Object updatePost(@RequestPart PostResponse postResponse, @RequestPart(required = false) MultipartFile[] filesToUpdate) throws PostNotFoundException, PostCannotUploadException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!postResponse.getUserId().equals(user.getId())) throw new RuntimeException("Người dùng không có quyền");

        Pair<Post, List<PostMedia>> request = postMapper.toPostAndListPostMedia(postResponse);
        if (request.b.isEmpty() && filesToUpdate == null) throw new PostCannotUploadException("Không thể đăng/chỉnh sửa bài viết nếu thiếu hình ảnh hoặc video!!!");
        Post updatedPost = postService.updatePost(request.a);
        List<Media> mediaList = mediaService.uploadFiles(filesToUpdate, updatedPost);
        Pair<List<String>, List<PostMedia>> files = postMediaService.updateFiles(request.b, mediaList, updatedPost);
        mediaService.deleteFiles(files.a);

        return postMapper.toResponse(updatedPost, Stream.concat(request.b.stream(), files.b.stream()).toList());
    }
}
