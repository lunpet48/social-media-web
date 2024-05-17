package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.MessageResponse;
import com.webapp.socialmedia.dto.responses.ShortProfileResponse;
import com.webapp.socialmedia.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MessageMapper {
    public MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .messageId(message.getId())
                .roomId(message.getRoom().getId())
                .mediaLink(message.getMediaLink())
                .message(message.getMessage())
                .sender(ShortProfileResponse.builder().userId(message.getUser().getId()).username(message.getUser().getUsername()).avatar(message.getUser().getProfile().getAvatar()).build())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
