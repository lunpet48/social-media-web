package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "db_message_media")
public class MessageMedia {
    @Id
    @OneToOne
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private Media media;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

}
