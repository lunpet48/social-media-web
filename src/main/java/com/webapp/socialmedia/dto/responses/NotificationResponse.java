package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.enums.NotificationStatus;
import com.webapp.socialmedia.enums.NotificationType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private ShortProfileResponse actor;
    private String receiver;
    private NotificationType notificationType;
    private String idType;
    private Date createdAt;
    private NotificationStatus status;
}
