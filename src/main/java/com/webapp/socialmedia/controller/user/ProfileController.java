package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final IProfileService profileService;

    @GetMapping
    public ResponseEntity<?> getProfile(String id){
        UserProfileResponse userProfileResponse = profileService.get(id);
        return ResponseEntity.ok(new ResponseDTO().success(userProfileResponse));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody ProfileRequest profileRequest){
        ProfileResponse profileResponse = profileService.update(profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO().success(profileResponse));
    }

    @PutMapping("avatar/{userId}")
    public ResponseEntity<?> updateAvatar(@RequestParam("file") MultipartFile multipartFile, @PathVariable String userId){
        ProfileResponse profileResponse = profileService.updateAvatar(userId, multipartFile);
        return ResponseEntity.ok(new ResponseDTO().success(profileResponse));
    }

    @PutMapping("background/{userId}")
    public ResponseEntity<?> updateBackground(@RequestParam("file") MultipartFile multipartFile, @PathVariable String userId){
        ProfileResponse profileResponse = profileService.updateBackground(userId, multipartFile);
        return ResponseEntity.ok(new ResponseDTO().success(profileResponse));
    }
}
