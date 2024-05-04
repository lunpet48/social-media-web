package com.webapp.socialmedia.repository;

import com.webapp.socialmedia.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {

}
