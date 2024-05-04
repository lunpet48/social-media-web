package com.webapp.socialmedia.controller.message;

import com.webapp.socialmedia.dto.requests.RoomRequest;
import com.webapp.socialmedia.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request.getName().trim()));
    }
}
