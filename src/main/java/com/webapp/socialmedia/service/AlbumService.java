package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.AlbumRequest;
import com.webapp.socialmedia.dto.responses.AlbumResponse;
import com.webapp.socialmedia.entity.Post;

import java.util.List;

public interface AlbumService {
    AlbumResponse createAlbum(AlbumRequest albumRequest);

    List<AlbumResponse> getAllMyAlbums();

    List<AlbumResponse> getAlbums(String userId);

    List<Post> getPostsInAlbum(String albumId);

    AlbumResponse changeAlbumName(String id, String name);

    void deleteAlbum(String albumId);

    void deletePostFromAlbum(String postId);

    AlbumResponse getAlbum(String albumId);
}
