package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.UserRequest;
import com.webapp.socialmedia.entity.Room;
import com.webapp.socialmedia.entity.User;

import java.util.List;

public interface ParticipantService {
    void addToRoom(List<UserRequest> userList, String roomId);
    void leaveRoom(List<UserRequest> userList, String roomId);
}
