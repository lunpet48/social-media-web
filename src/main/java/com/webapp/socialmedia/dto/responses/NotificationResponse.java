package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.enums.NotificationType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String actor;
    private String receiver;
    private NotificationType notificationType;
    private Date createdAt;
}
