package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.dto.responses.UserResponse;
import com.webapp.socialmedia.entity.Profile;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.UserNotMatchTokenException;
import com.webapp.socialmedia.mapper.ProfileMapper;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.ProfileRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.IProfileService;
import com.webapp.socialmedia.validattion.serviceValidation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserValidationService userValidationService;

    @Override
    public ProfileResponse update(ProfileRequest profileRequest) {
        if(!userValidationService.isUserMatchToken(profileRequest.getUserId()))
            throw new UserNotMatchTokenException();

        Profile profile = ProfileMapper.INSTANCE.ProfileRequestToProfile(profileRequest);
        profileRepository.save(profile);
        return ProfileMapper.INSTANCE.ProfileToProfileResponse(profile);
    }

    @Override
    public UserResponse get(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->new UsernameNotFoundException("User Not Found"));
        return UserMapper.INSTANCE.userToUserResponse(user);
    }
}
