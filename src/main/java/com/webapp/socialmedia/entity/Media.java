package com.webapp.socialmedia.entity;

import com.webapp.socialmedia.enums.MediaType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_media")
public class Media {
    @Id
    private String id;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Builder.Default
    private Boolean isDeleted = false;
}
