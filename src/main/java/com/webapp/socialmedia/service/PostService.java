package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.AlbumShortRequest;
import com.webapp.socialmedia.dto.requests.PostRequest;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.PostMedia;
import com.webapp.socialmedia.exceptions.PostCannotUploadException;
import com.webapp.socialmedia.exceptions.PostNotFoundException;
import com.webapp.socialmedia.exceptions.UserNotAuthoritativeException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Post createPost(PostRequest postRequest);

    Post createPost(PostRequest postRequest, AlbumShortRequest albumShortRequest);

    Post updatePost(Post post, List<PostMedia> postMediaList, MultipartFile[] files, String userId, AlbumShortRequest albumShortRequest) throws PostNotFoundException, PostCannotUploadException;

    void deletePost(String postId, String userId) throws PostNotFoundException, UserNotAuthoritativeException;

    Post getPost(String postId, String userId) throws PostNotFoundException;

    List<Post> getListPostByUserIdAndIsDeleted(String userId);

    List<Post> getHomepage(int pageSize, int pageNo);

    List<UserProfileResponse> getLikesOfPost(String postId);

    Post sharePost(PostRequest sharedPostRequest);

    List<Post> getAllSharedPost(String userId);
}
