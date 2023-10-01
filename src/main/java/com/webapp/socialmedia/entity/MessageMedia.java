package com.webapp.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_message_media")
public class MessageMedia {
    @Id
    @OneToOne
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private Media mediaId;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message messageId;

}
