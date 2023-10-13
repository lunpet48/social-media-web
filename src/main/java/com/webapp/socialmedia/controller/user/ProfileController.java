package com.webapp.socialmedia.controller.user;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
//    private final IProfileService profileService;
//    @PostMapping
//    public ResponseEntity<?> createProfile(ProfileRequest profileRequest){
//        ProfileResponse profileResponse = profileService.create(profileRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body(profileResponse);
//    }

//    public ResponseEntity<?> updateProfile(){//patch
//        return ResponseEntity.ok("");
//    }
}
