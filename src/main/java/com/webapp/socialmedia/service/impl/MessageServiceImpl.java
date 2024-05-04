package com.webapp.socialmedia.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.webapp.socialmedia.dto.requests.MessageRequest;
import com.webapp.socialmedia.dto.responses.MessageResponse;
import com.webapp.socialmedia.entity.Message;
import com.webapp.socialmedia.entity.Participant;
import com.webapp.socialmedia.entity.Room;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.mapper.MessageMapper;
import com.webapp.socialmedia.repository.MessageRepositoty;
import com.webapp.socialmedia.repository.ParticipantRepository;
import com.webapp.socialmedia.repository.RoomRepository;
import com.webapp.socialmedia.service.MessageService;
import com.webapp.socialmedia.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final MessageRepositoty messageRepositoty;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Cloudinary cloudinary;
    private final MessageMapper mapper;

    @Override
    public MessageResponse sendMessage(MessageRequest messageRequest, MultipartFile file) throws IOException {
        Room room = roomRepository.findById(messageRequest.getRoomId()).orElseThrow(() -> new BadRequestException("Không tìm thấy cuộc trò chuyện"));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Message message;
        if(file != null && !file.isEmpty()){
            var u = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("public_id", UUID.randomUUID().toString(),
                            "resource_type", "auto",
                            "folder", user.getId() + "/CHAT"));
            message = Message.builder()
                    .message(messageRequest.getMessage())
                    .room(room)
                    .mediaLink(u.get("secure_url").toString())
                    .user(user)
                    .build();
        } else {
            message = Message.builder()
                    .message(messageRequest.getMessage())
                    .room(room)
                    .mediaLink("")
                    .user(user)
                    .build();
        }
        Message temp = messageRepositoty.saveAndFlush(message);

        List<Participant> participants = participantRepository.findParticipantByRoom_Id(room.getId());

        //Thông báo
        participants.forEach(participant -> {
            simpMessagingTemplate.convertAndSendToUser(participant.getUser().getUsername(), NotificationUtils.CHAT_LINK, mapper.toResponse(temp));
        });

        return mapper.toResponse(temp);
    }

    @Override
    public List<MessageResponse> loadMessageInRoom(String roomId, int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Message> result = messageRepositoty.findByRoom_IdOrderByCreatedAtDesc(roomId, paging);
        if(result.hasContent()) {
            return result.getContent().stream().map(mapper::toResponse).toList();
        }
        return new ArrayList<>();
    }
}
