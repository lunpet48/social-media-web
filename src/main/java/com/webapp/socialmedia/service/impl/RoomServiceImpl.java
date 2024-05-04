package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.entity.Room;
import com.webapp.socialmedia.repository.RoomRepository;
import com.webapp.socialmedia.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository repository;

    @Override
    public Room createRoom(String name) {
        Room temp = Room.builder().name(name).build();
        return repository.saveAndFlush(temp);
    }
}
