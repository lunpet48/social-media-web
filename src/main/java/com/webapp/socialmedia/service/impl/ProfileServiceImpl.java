package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.entity.Profile;
import com.webapp.socialmedia.mapper.IProfileMapper;
import com.webapp.socialmedia.repository.ProfileRepository;
import com.webapp.socialmedia.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private final ProfileRepository profileRepository;
    private final IProfileMapper profileMapper;
    @Override
    public ProfileResponse create(ProfileRequest profileRequest) {
        Profile profile = profileMapper.ProfileRequestToProfile(profileRequest);
        profileRepository.save(profile);
        return profileMapper.ProfileToProfileResponse(profile);
    }

    @Override
    public ProfileResponse update(ProfileRequest profileRequest) {
        Profile profile = profileMapper.ProfileRequestToProfile(profileRequest);
        profileRepository.save(profile);
        return profileMapper.ProfileToProfileResponse(profile);
    }

    @Override
    public ProfileResponse get(String userId){
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() ->new UsernameNotFoundException("User Not Found"));
        return profileMapper.ProfileToProfileResponse(profile);
    }
}
