package com.webapp.socialmedia.controller.notification;

import com.webapp.socialmedia.dto.requests.NotificationRequest;
import com.webapp.socialmedia.dto.requests.Paging;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.NotificationStatus;
import com.webapp.socialmedia.enums.NotificationType;
import com.webapp.socialmedia.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public ResponseEntity<?> getNotification(@RequestParam String notificationId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new ResponseDTO().success(notificationService.getAnNotification(notificationId)));
    }

    @GetMapping("/notification/{type}")
    public ResponseEntity<?> getNotificationByType(@PathVariable String type, @RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
        if(type.equals("post"))
            return ResponseEntity.ok(new ResponseDTO().success(notificationService.findByNotificationType(Arrays.asList(NotificationType.LIKE, NotificationType.COMMENT) , pageNo, pageSize)));
        else if(type.equals("friend"))
            return ResponseEntity.ok(new ResponseDTO().success(notificationService.findByNotificationType(Arrays.asList(NotificationType.FRIEND_ACCEPT, NotificationType.FRIEND_REQUEST) , pageNo, pageSize)));
        else if(type.equals("comment")){
            return ResponseEntity.ok(new ResponseDTO().success(notificationService.findByNotificationType(List.of(NotificationType.COMMENT), pageNo, pageSize)));
        } else if (type.equals("tag")) {
            return ResponseEntity.ok(new ResponseDTO().success(notificationService.findByNotificationType(List.of(NotificationType.MENTION_IN_POST, NotificationType.MENTION_IN_COMMENT), pageNo, pageSize)));
        } else {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }
    }

//    @GetMapping("/notification")
//    public ResponseEntity<?> readNotification()
}
