package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.requests.ChangePasswordRequest;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    @PostMapping("/changePassword")
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changePassword(changePasswordRequest, user.getId());
    }


    @GetMapping("recommend")
    public ResponseEntity<?> getRecommendUsers(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<UserProfileResponse> userProfileResponses = userService.getRecommendUsers(user.getId());
        return ResponseEntity.ok(new ResponseDTO().success(userProfileResponses));
    }
}
