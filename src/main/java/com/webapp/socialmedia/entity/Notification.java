package com.webapp.socialmedia.entity;

import com.webapp.socialmedia.enums.NotificationStatus;
import com.webapp.socialmedia.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_notification")
public class Notification {
    @Id
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name="actor", nullable = false)
    private User actor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String idType;

    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @PrePersist
    private void createdAt() {
        this.createdAt = new Date();
        this.status = NotificationStatus.UNREAD;
    }
}
