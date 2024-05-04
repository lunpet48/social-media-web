package com.webapp.socialmedia.controller.message;

import com.webapp.socialmedia.dto.requests.ParticipantRequest;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping("/participant")
    public ResponseEntity<?> addToRoom(@RequestBody ParticipantRequest request) {
        participantService.addToRoom(request.getUserList(), request.getRoomId());
        return ResponseEntity.ok(new ResponseDTO().success("Thêm người dùng thành công"));
    }

    @DeleteMapping("/participant")
    public ResponseEntity<?> leaveRoom(@RequestBody ParticipantRequest request) {
        participantService.leaveRoom(request.getUserList(), request.getRoomId());
        return ResponseEntity.ok(new ResponseDTO().success("Rời cuộc trò chuyện thành công"));
    }
}
