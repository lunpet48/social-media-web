package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IProfileService {
    ProfileResponse update(ProfileRequest profileRequest, String userId);

    UserProfileResponse get(String userId);

    ProfileResponse updateAvatar(String userId,MultipartFile multipartFile);

    ProfileResponse updateBackground(String userId, MultipartFile multipartFile);
}
