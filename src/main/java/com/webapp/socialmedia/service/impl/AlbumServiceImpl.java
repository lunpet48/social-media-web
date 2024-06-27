package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.AlbumRequest;
import com.webapp.socialmedia.dto.requests.ShortPostRequest;
import com.webapp.socialmedia.dto.responses.AlbumResponse;
import com.webapp.socialmedia.entity.Album;
import com.webapp.socialmedia.entity.Post;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.mapper.AlbumMapper;
import com.webapp.socialmedia.repository.AlbumRepository;
import com.webapp.socialmedia.repository.PostRepository;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final PostRepository postRepository;
    private final RelationshipRepository relationshipRepository;
    private final AlbumMapper albumMapper;
    @Override
    public AlbumResponse createAlbum(AlbumRequest albumRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Album album;
        if(albumRequest.getId() == null || albumRequest.getId().isBlank()) {
             album = albumRepository.saveAndFlush(Album.builder()
                            .user(user)
                            .name(albumRequest.getName()).build());
        }

        else {
            album = albumRepository.findByIdAndIsDeleted(albumRequest.getId(), Boolean.FALSE).orElseThrow(() -> new BadRequestException("Không tìm thấy album!!!"));
        }

        for (ShortPostRequest postRequest : albumRequest.getPosts()) {
            Post post = postRepository.findByIdAndIsDeleted(postRequest.getPostId(), Boolean.FALSE).orElse(null);
            if(post == null) continue;
            post.setAlbum(album);
            postRepository.save(post);
        }

        return albumMapper.toResponse(album);
    }

    @Override
    public List<AlbumResponse> getAllMyAlbums() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Album> albums = albumRepository.findByUser_IdAndIsDeleted(user.getId(), Boolean.FALSE);

        return albums.stream().map(albumMapper::toResponse).toList();
    }

    @Override
    public List<AlbumResponse> getAlbums(String userId) {
        List<Album> albums = albumRepository.findByUser_IdAndIsDeleted(userId, Boolean.FALSE);

        return albums.stream().map(albumMapper::toResponse).toList();
    }

    @Override
    public List<Post> getPostsInAlbum(String albumId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Album album = albumRepository.findByIdAndIsDeleted(albumId, Boolean.FALSE).orElseThrow(() -> new BadRequestException("Không tìm thấy album!!!"));

        if (currentUser.getId().equals(album.getUser().getId()))
            return postRepository.findByAlbum_IdAndIsDeletedOrderByCreatedAtDesc(album.getId(), Boolean.FALSE);
        Optional<Relationship> relationship = relationshipRepository.findByUserIdAndRelatedUserId(album.getUser().getId(), currentUser.getId());

        if (relationship.isPresent()) {
            if (relationship.get().getStatus().equals(RelationshipStatus.FRIEND))
                return postRepository.findPostsWithFriendsInAlbum(album.getUser().getId(), albumId);
            else if (relationship.get().getStatus().equals(RelationshipStatus.BLOCK))
                throw new UserNotFoundException();
        }

        return postRepository.findPostWithPublicInAlbum(album.getUser().getId(), albumId);

    }

    @Override
    public AlbumResponse changeAlbumName(String id, String name) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Album album = albumRepository.findByIdAndIsDeleted(id, Boolean.FALSE).orElseThrow(() -> new BadRequestException("Không tìm thấy album"));
        if(album.getUser().getId().equals(currentUser.getId())){
            album.setName(name.trim());
            Album newAlbum = albumRepository.saveAndFlush(album);
            return AlbumResponse.builder().userId(newAlbum.getUser().getId())
                    .name(newAlbum.getName())
                    .id(newAlbum.getId())
                    .build();
        }
        throw new BadRequestException("Album của bạn không tồn tại!!");
    }

    @Override
    public void deleteAlbum(String albumId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Album album = albumRepository.findByIdAndIsDeleted(albumId, Boolean.FALSE).orElseThrow(() -> new BadRequestException("Không tìm thấy album"));
        if(album.getUser().getId().equals(currentUser.getId())){
            album.setIsDeleted(true);
            albumRepository.saveAndFlush(album);
            List<Post> posts = postRepository.findByAlbum_IdAndIsDeleted(albumId, Boolean.FALSE);
            posts.forEach(post -> {
                post.setAlbum(null);
                postRepository.saveAndFlush(post);
            });
        }
    }

    @Override
    public void deletePostFromAlbum(String postId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findByIdAndIsDeleted(postId, Boolean.FALSE).orElseThrow(() -> new BadRequestException("Không tìm thấy bài viết"));
        if(currentUser.getId().equals(post.getUser().getId())) {
            post.setAlbum(null);
            postRepository.saveAndFlush(post);
        }
    }

    @Override
    public AlbumResponse getAlbum(String albumId) {
        Album album = albumRepository.findByIdAndIsDeleted(albumId, Boolean.FALSE).orElseThrow(() -> new BadRequestException("Không tìm thấy album"));
        return albumMapper.toResponse(album);
    }
}
