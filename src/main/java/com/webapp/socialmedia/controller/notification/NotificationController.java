package com.webapp.socialmedia.controller.notification;

import com.webapp.socialmedia.dto.requests.NotificationRequest;
import com.webapp.socialmedia.dto.requests.Paging;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.NotificationStatus;
import com.webapp.socialmedia.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/all-notification")
    public ResponseEntity<?> getNotificationByReceiverId(@RequestParam int pageSize, @RequestParam int pageNo) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new ResponseDTO().success(
                notificationService.findByReceiver(user.getId(),
                        pageNo, pageSize)));
    }

    @GetMapping("/unread-notification")
    public ResponseEntity<?> getUnreadNotification(@RequestParam int pageSize, @RequestParam int pageNo) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new ResponseDTO().success(
                notificationService.findByStatus(user.getId(), NotificationStatus.UNREAD, pageNo, pageSize)));
    }

    @GetMapping("/amount-new-notification")
    public ResponseEntity<?> returnAmountOfNewNotification() {
        return ResponseEntity.ok(new ResponseDTO().success(notificationService.returnAmountOfNewNotification()));
    }

    @GetMapping("/notification")
    public ResponseEntity<?> getNotification(@RequestBody NotificationRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new ResponseDTO().success(notificationService.getAnNotification(request)));
    }

//    @GetMapping("/notification")
//    public ResponseEntity<?> readNotification()
}
