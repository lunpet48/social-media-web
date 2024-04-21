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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/all-notification")
    public ResponseEntity<?> getNotificationByReceiverId(@RequestBody Paging request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new ResponseDTO().success(
                notificationService.findByReceiver(user.getId(),
                        request.getPageNo(), request.getPageSize())));
    }

    @GetMapping("/unread-notification")
    public ResponseEntity<?> getUnreadNotification(@RequestBody Paging request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new ResponseDTO().success(
                notificationService.findByStatus(user.getId(), NotificationStatus.UNREAD, request.getPageNo(), request.getPageSize())));
    }

    @GetMapping("/amount-new-notification")
    public ResponseEntity<?> returnAmountOfNewNotification() {
        return ResponseEntity.ok(new ResponseDTO().success(notificationService.returnAmountOfNewNotification()));
    }

//    @GetMapping("/notification")
//    public ResponseEntity<?> getNotification(@RequestBody NotificationRequest request) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        notificationService.
//    }

//    @GetMapping("/notification")
//    public ResponseEntity<?> readNotification()
}
