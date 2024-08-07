package com.webapp.socialmedia.controller.message;

import com.webapp.socialmedia.dto.requests.MessageRequest;
import com.webapp.socialmedia.dto.requests.UserRequest;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestPart MessageRequest message, @RequestPart(required = false) MultipartFile file) throws IOException {
       return ResponseEntity.ok(new ResponseDTO().success(messageService.sendMessage(message, file)));
    }

    @GetMapping("/chat/{roomId}")
    public ResponseEntity<?> loadMessageInChat(@PathVariable String roomId, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(new ResponseDTO().success(messageService.loadMessageInRoom(roomId, pageNo, pageSize)));
    }

    @PostMapping("/chat/{roomId}")
    public void markMessageAsRead(@PathVariable String roomId) {
        messageService.markMessageAsRead(roomId);
    }

    @GetMapping("/chat")
    public ResponseEntity<?> getChat() {
        return ResponseEntity.ok(new ResponseDTO().success(messageService.loadRoomChatByUser()));
    }

    @PostMapping("/chat/check")
    public ResponseEntity<?> postChat(@RequestBody List<UserRequest> requests) {
        return ResponseEntity.ok(new ResponseDTO().success(messageService.addToRoomOrReturnAlreadyRoom(requests)));
    }

    @PostMapping("/chat/search")
    public ResponseEntity<?> searchChat(@RequestParam String keyword) {
        return ResponseEntity.ok(new ResponseDTO().success(messageService.searchRoom(keyword)));
    }
}

