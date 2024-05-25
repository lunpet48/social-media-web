package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, String> {
    Optional<Album> findByIdAndIsDeleted(String id, Boolean isDeleted);

    List<Album> findByUser_IdAndIsDeleted(String id, Boolean isDeleted);
}
