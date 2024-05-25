package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.AlbumResponse;
import com.webapp.socialmedia.entity.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AlbumMapper {
    public AlbumResponse toResponse(Album album) {
        return AlbumResponse.builder()
                .userId(album.getUser().getId())
                .id(album.getId())
                .name(album.getName())
                .build();
    }
}
