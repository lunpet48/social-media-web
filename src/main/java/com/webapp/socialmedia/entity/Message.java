package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_message")
public class Message {
    @Id
    @UuidGenerator
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room roomId;

    private String message;
    private Date createdAt;
    @PrePersist
    private void createdAt() {
        this.createdAt = new Date();
    }
}
