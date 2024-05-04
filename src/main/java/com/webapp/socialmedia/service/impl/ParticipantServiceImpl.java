package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.UserRequest;
import com.webapp.socialmedia.entity.Participant;
import com.webapp.socialmedia.entity.ParticipantId;
import com.webapp.socialmedia.entity.Room;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.repository.ParticipantRepository;
import com.webapp.socialmedia.repository.RoomRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.ParticipantService;
import com.webapp.socialmedia.utils.NotificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Override
    public void addToRoom(List<UserRequest> userList, String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Không tìm thấy phòng trò chuyện"));
        userList.forEach(userRequest -> {
            User temp = userRepository.findById(userRequest.getUserId()).orElseThrow(UserNotFoundException::new);
            Participant participant = Participant.builder().room(room).user(temp).build();
            participantRepository.save(participant);
        });

        //Thông báo
        List<Participant> participants = participantRepository.findParticipantByRoom_Id(roomId);

        List<String> message = new ArrayList<>();

        participants.forEach(participant -> {
            message.add(participant.getUser().getUsername() + " đã được thêm vào cuộc trò chuyện");
        });

        participants.forEach(participant -> {
            simpMessagingTemplate.convertAndSendToUser(participant.getUser().getUsername(), NotificationUtils.CHAT_LINK, message);
        });
    }

    @Override
    public void leaveRoom(List<UserRequest> userList, String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Không tìm thấy phòng trò chuyện"));
        userList.forEach(userRequest -> {
            User temp = userRepository.findById(userRequest.getUserId()).orElseThrow(UserNotFoundException::new);
            Participant participant = participantRepository.findById(ParticipantId.builder().room(room).user(temp).build()).orElseThrow(() -> new BadRequestException("Không tìm thấy người dùng/phòng trò chuyện"));
            participantRepository.delete(participant);
        });
    }
}
