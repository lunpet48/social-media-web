package com.webapp.socialmedia.entity;

import com.webapp.socialmedia.enums.EventType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "db_log")
public class Log {
    @Id
    @UuidGenerator
    private String id;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String objectId;
    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
