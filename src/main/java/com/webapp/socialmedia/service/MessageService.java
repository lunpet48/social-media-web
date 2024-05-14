package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.MessageRequest;
import com.webapp.socialmedia.dto.requests.UserRequest;
import com.webapp.socialmedia.dto.responses.MessageResponse;
import com.webapp.socialmedia.dto.responses.MessageResponseV2;
import com.webapp.socialmedia.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest messageRequest, MultipartFile file) throws IOException;

    List<MessageResponse> loadMessageInRoom(String roomId, int pageNo, int pageSize);

    List<MessageResponseV2> loadRoomChatByUser();

    Room addToRoomOrReturnAlreadyRoom(List<UserRequest> requests);
}
