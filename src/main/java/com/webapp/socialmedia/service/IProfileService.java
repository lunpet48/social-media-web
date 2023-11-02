package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;

public interface IProfileService {
    ProfileResponse update(ProfileRequest profileRequest);

    UserProfileResponse get(String userId);
}
