package com.webapp.socialmedia.dto.requests;

import com.webapp.socialmedia.enums.NotificationType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
   private String id;
   private String receiverId;
   private String actorId;
   private NotificationType notificationType;
   private String idType;
   private Date createdAt;
}
