package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;

public interface IProfileService {
    ProfileResponse create(ProfileRequest profileRequest);

    ProfileResponse update(ProfileRequest profileRequest);

    ProfileResponse get(String userId);
}
