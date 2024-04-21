package com.webapp.socialmedia.dto.requests;

import com.webapp.socialmedia.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
   private String receiverId;
   private String actorId;
   private NotificationType notificationType;
   private String idType;
}
