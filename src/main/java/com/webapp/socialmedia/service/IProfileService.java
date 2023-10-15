package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.dto.responses.UserResponse;

public interface IProfileService {
    ProfileResponse update(ProfileRequest profileRequest);

    UserResponse get(String userId);
}
