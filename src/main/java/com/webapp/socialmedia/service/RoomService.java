package com.webapp.socialmedia.service;

import com.webapp.socialmedia.entity.Room;

public interface RoomService {
    Room createRoom(String name);
    Room changeName(String roomId, String roomName);
}
