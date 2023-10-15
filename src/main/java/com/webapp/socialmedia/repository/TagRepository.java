package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, String> {
}
