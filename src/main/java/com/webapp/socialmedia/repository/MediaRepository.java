package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, String> {

}
