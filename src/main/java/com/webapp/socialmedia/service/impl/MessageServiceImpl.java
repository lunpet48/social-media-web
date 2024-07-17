package com.webapp.socialmedia.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.webapp.socialmedia.dto.requests.MessageRequest;
import com.webapp.socialmedia.dto.requests.UserRequest;
import com.webapp.socialmedia.dto.responses.MessageResponse;
import com.webapp.socialmedia.dto.responses.ChatRoom;
import com.webapp.socialmedia.dto.responses.RoomResponse;
import com.webapp.socialmedia.dto.responses.ShortProfileResponse;
import com.webapp.socialmedia.entity.*;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.mapper.MessageMapper;
import com.webapp.socialmedia.repository.*;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final RelationshipRepository relationshipRepository;
    private final MessageRepositoty messageRepositoty;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Cloudinary cloudinary;
    private final MessageMapper mapper;

    @Override
    public MessageResponse sendMessage(MessageRequest messageRequest, MultipartFile file) throws IOException {
        Room room = roomRepository.findById(messageRequest.getRoomId()).orElseThrow(() -> new BadRequestException("Không tìm thấy cuộc trò chuyện"));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Participant> participants = participantRepository.findParticipantByRoom_Id(room.getId());

        if(participants.size() == 2) {
            participants.forEach(participant -> {
                Optional<Relationship> temp = relationshipRepository.findByUserIdAndRelatedUserId(user.getId(), participant.getUser().getId());

                if (temp.isPresent() && (temp.get().getStatus().equals(RelationshipStatus.BLOCK) || temp.get().getStatus().equals(RelationshipStatus.BLOCKED))) {
                    throw new BadRequestException("Không thể gửi tin nhắn");
                }
            });
        }

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
        Participant x = participantRepository.findParticipantByRoom_IdAndUserId(room.getId(), user.getId());
        x.setLatestReadMessageId(temp.getId());
        participantRepository.save(x);
        //Thông báo
        participants.forEach(participant -> {
            simpMessagingTemplate.convertAndSendToUser(participant.getUser().getUsername(), NotificationUtils.CHAT_LINK, mapper.toResponse(temp));
        });

        return mapper.toResponse(temp);
    }

    @Override
    public ChatRoom loadMessageInRoom(String roomId, int pageNo, int pageSize) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Message> result = messageRepositoty.findByRoom_IdOrderByCreatedAtDesc(roomId, paging);

//        Optional<Message> message = messageRepositoty.findTopByRoomIdOrderByCreatedAtDesc(roomId);
//        Participant user = participantRepository.findParticipantByRoom_IdAndUserId(roomId, currentUser.getId());
//        message.ifPresent(value -> user.setLatestReadMessageId(value.getId()));
//        participantRepository.save(user);

        return ChatRoom.builder()
                .roomId(roomId)
                .message(result.hasContent() ? result.getContent().stream().map(mapper::toResponse).toList() : new ArrayList<>())
                .users(participantRepository.findParticipantByRoom_Id(roomId).stream().map(participant -> ShortProfileResponse.builder()
                        .userId(participant.getUser().getId())
                        .avatar(participant.getUser().getProfile().getAvatar())
                        .username(participant.getUser().getUsername())
                        .build()).toList())
//                        .isRead(true)
                .build();
    }

    @Override
    public List<ChatRoom> loadRoomChatByUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MessageResponse> response = new ArrayList<>();
        List<Map<String, Object>> temp = messageRepositoty.loadRoomsByUserId(user.getId());
        for (Map<String, Object> map: temp){
            Object date = map.get("createdAt");
            Object roomId = map.get("room_id");
            Optional<Message> x = messageRepositoty.findByRoom_IdAndCreatedAt((String) roomId, (Date) date);
            if(x.isPresent()){
                response.add(mapper.toResponse(x.get()));
            }
        }
        List<ChatRoom> responseV2s = new ArrayList<>();

        for(MessageResponse messageResponse : response) {
            List<ShortProfileResponse> profileResponseV2s = new ArrayList<>();
            List<Participant> participants = participantRepository.findParticipantByRoom_Id(messageResponse.getRoomId());
            Participant x = participantRepository.findParticipantByRoom_IdAndUserId(messageResponse.getRoomId() , user.getId());
            boolean isRead = true;
            if(messageResponse.getMessageId() != null) {
                if (x.getLatestReadMessageId() == null || !x.getLatestReadMessageId().equals(messageResponse.getMessageId())) {
                    isRead = false;
                }
            }
            for(Participant participant : participants) {
                ShortProfileResponse profile = ShortProfileResponse.builder().avatar(participant.getUser().getProfile().getAvatar())
                        .username(participant.getUser().getUsername())
                        .userId(participant.getUser().getId())
                        .build();

                profileResponseV2s.add(profile);
            }
            responseV2s.add(ChatRoom.builder().users(profileResponseV2s).message(List.of(messageResponse)).roomId(messageResponse.getRoomId()).isRead(isRead).build());
        }

        return responseV2s;
        //return response.stream().map(mapper::toResponse).toList();
    }

    @Override
    public ChatRoom addToRoomOrReturnAlreadyRoom(List<UserRequest> requests) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(requests.size() == 1) {
            //Tìm các participant/room hiện tại của current user
            List<Participant> x = participantRepository.findParticipantByUser_Id(user.getId());
            //Nếu có participant == 2 && participant có user id == request thì trả về kq
            for (Participant temp : x) {
                List<Participant> y = participantRepository.findParticipantByRoom_Id(temp.getRoom().getId());
                if (y.size() != 2) continue;
                for(Participant participant : y){
                    if(participant.getUser().getId().equals(requests.get(0).getUserId())) {
                        return ChatRoom.builder()
                                .roomId(participant.getRoom().getId())
                                .message(messageRepositoty.findByRoom_IdOrderByCreatedAtDesc(participant.getRoom().getId(), PageRequest.of(0, 10)).stream().map(mapper::toResponse).toList())
                                .users(y.stream().map(participant1 -> ShortProfileResponse.builder()
                                        .avatar(participant1.getUser().getProfile().getAvatar())
                                        .username(participant1.getUser().getUsername())
                                        .userId(participant1.getUser().getId())
                                        .build()).toList()).build();
                    }
                }
            }

            //Nếu không có
            User relatedUser = userRepository.findById(requests.get(0).getUserId()).orElseThrow(UserNotFoundException::new);
            Room room = roomRepository.saveAndFlush(Room.builder().build());
            Participant participant1 = participantRepository.saveAndFlush(Participant.builder().room(room).user(user).build());
            Participant participant2 = participantRepository.saveAndFlush(Participant.builder().room(room).user(relatedUser).build());
            return ChatRoom.builder()
                    .roomId(room.getId())
                    .message(messageRepositoty.findByRoom_IdOrderByCreatedAtDesc(room.getId(), PageRequest.of(0, 10)).stream().map(mapper::toResponse).toList())
                    .users(List.of(ShortProfileResponse.builder()
                                    .userId(participant1.getUser().getId())
                                    .avatar(participant1.getUser().getProfile().getAvatar())
                                    .username(participant1.getUser().getUsername())
                            .build(),
                            ShortProfileResponse.builder()
                                    .username(participant2.getUser().getUsername())
                                    .avatar(participant2.getUser().getProfile().getAvatar())
                                    .userId(participant2.getUser().getId())
                                    .build()))
                    .build();
        } else {
            requests.add(UserRequest.builder().userId(user.getId()).build());
            Room room = roomRepository.saveAndFlush(Room.builder().build());
            List<ShortProfileResponse> listUser = new ArrayList<>();
            for (UserRequest userRequest : requests) {
                User userTemp = userRepository.findById(userRequest.getUserId()).orElseThrow(UserNotFoundException::new);
                participantRepository.saveAndFlush(Participant.builder().room(room).user(userTemp).build());
                listUser.add(ShortProfileResponse.builder()
                                .username(userTemp.getUsername())
                                .avatar(userTemp.getProfile().getAvatar())
                                .userId(userTemp.getId())
                        .build());
            }
            return ChatRoom.builder()
                    .users(listUser)
                    .message(null)
                    .roomId(room.getId())
                    .build();
        }
    }

    @Override
    public List<RoomResponse> searchRoom(String keyword) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Participant> participantList = participantRepository.findParticipantByUser_Id(currentUser.getId());
        List<RoomResponse> rooms = new ArrayList<>();
        for(Participant participant : participantList) {
            List<Participant> temp = participantRepository.findParticipantByRoom_Id(participant.getRoom().getId());
            List<ShortProfileResponse> shortProfileResponses = new ArrayList<>();
            if(temp.size() == 2) {
                for(Participant participant1: temp){
                    shortProfileResponses.add(ShortProfileResponse.builder()
                        .username(participant1.getUser().getUsername())
                        .userId(participant1.getUser().getId())
                        .avatar(participant1.getUser().getProfile().getAvatar())
                        .build());
                }
                for(Participant participant1: temp) {
                    if(participant1.getUser().getUsername().toLowerCase().contains(keyword.toLowerCase()) && !currentUser.getUsername().equals(participant1.getUser().getUsername())) {
                        rooms.add(RoomResponse.builder()
                                .roomId(participant.getRoom().getId())
                                .name(participant.getRoom().getName())
                                .users(shortProfileResponses)
                                .build());
                    }
                }
            }
            else {
                if(participant.getRoom().getName() != null && participant.getRoom().getName().toLowerCase().contains(keyword.toLowerCase())){
                    for(Participant participant1: temp) {
                        shortProfileResponses.add(ShortProfileResponse.builder()
                                .avatar(participant1.getUser().getProfile().getAvatar())
                                .userId(participant1.getUser().getId())
                                .username(participant1.getUser().getUsername())
                                .build());
                    }
                    rooms.add(RoomResponse.builder()
                            .roomId(participant.getRoom().getId())
                            .users(shortProfileResponses)
                            .build());
                }

            }
        }
        return rooms;
    }

    @Override
    public void markMessageAsRead(String roomId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Message> message = messageRepositoty.findTopByRoomIdOrderByCreatedAtDesc(roomId);
        Participant user = participantRepository.findParticipantByRoom_IdAndUserId(roomId, currentUser.getId());
        message.ifPresent(value -> user.setLatestReadMessageId(value.getId()));
        participantRepository.save(user);
    }
}
