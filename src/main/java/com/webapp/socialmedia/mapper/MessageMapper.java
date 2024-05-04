package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.MessageResponse;
import com.webapp.socialmedia.entity.Message;
import com.webapp.socialmedia.entity.MessageMedia;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class MessageMapper {
    public MessageResponse toResponse(Message message) {
        return MessageResponse.builder()
                .messageId(message.getId())
                .roomId(message.getRoom().getId())
                .mediaLink(message.getMediaLink())
                .message(message.getMessage())
                .userId(message.getUser().getId())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
