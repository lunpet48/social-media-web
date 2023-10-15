package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.RelationshipId;
import com.webapp.socialmedia.enums.RelationshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {
    Optional<Relationship> findByIdAndStatus(RelationshipId id, RelationshipStatus status);
}
