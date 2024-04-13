package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.NotificationResponse;
import com.webapp.socialmedia.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "notification.receiver.username", target = "receiver")
    @Mapping(source = "notification.actor.username", target = "actor")
    @Mapping(source = "notification.notificationType", target = "notificationType")
    @Mapping(source = "notification.createdAt", target = "createdAt")
    NotificationResponse toResponse(Notification notification);
}
