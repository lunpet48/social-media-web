package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.MessageRequest;
import com.webapp.socialmedia.dto.requests.UserRequest;
import com.webapp.socialmedia.dto.responses.MessageResponse;
import com.webapp.socialmedia.dto.responses.ChatRoom;
import com.webapp.socialmedia.dto.responses.RoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest messageRequest, MultipartFile file) throws IOException;

    ChatRoom loadMessageInRoom(String roomId, int pageNo, int pageSize);

    List<ChatRoom> loadRoomChatByUser();

    ChatRoom addToRoomOrReturnAlreadyRoom(List<UserRequest> requests);

    List<RoomResponse> searchRoom(String keyword);

    void markMessageAsRead(String roomId);
}
