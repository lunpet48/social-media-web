package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.NotificationResponse;
import com.webapp.socialmedia.dto.responses.ProfileResponseV2;
import com.webapp.socialmedia.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper {

//    @Mapping(source = "notification.id", target = "id")
//    @Mapping(source = "notification.receiver.id", target = "receiver")
//    @Mapping(source = "notification.actor.id", target = "actor")
//    @Mapping(source = "notification.notificationType", target = "notificationType")
//    @Mapping(source = "notification.idType", target = "idType")
//    @Mapping(source = "notification.createdAt", target = "createdAt")
    public NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = NotificationResponse.builder()
                .actor(ProfileResponseV2.builder().avatar(notification.getActor().getProfile().getAvatar())
                        .username(notification.getActor().getUsername())
                        .userId(notification.getActor().getId())
                        .build())
                .createdAt(notification.getCreatedAt())
                .id(notification.getId())
                .idType(notification.getIdType())
                .notificationType(notification.getNotificationType())
                .receiver(notification.getReceiver().getId())
                .build();

        return response;
    }
}
