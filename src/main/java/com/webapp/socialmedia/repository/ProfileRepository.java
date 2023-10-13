package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {
}
