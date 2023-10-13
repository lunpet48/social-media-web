package com.webapp.socialmedia.mapper.impl;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.entity.Profile;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.mapper.IProfileMapper;
import com.webapp.socialmedia.repository.ProfileRepository;
import com.webapp.socialmedia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Mapper
public class ProfileMapperImpl implements IProfileMapper {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public Profile ProfileRequestToProfile(ProfileRequest profileRequest){
        User user = userRepository.findById(profileRequest.getUserId())
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));
        return Profile
                .builder()
                .user(user)
                .bio(profileRequest.getBio())
                .avatar(profileRequest.getAvatar())
                .fullName(profileRequest.getFullName())
                .build();
    }

    @Override
    public ProfileResponse ProfileToProfileResponse(Profile profile){
        return ProfileResponse
                .builder()
                .userId(profile.getUser().getId())
                .avatar(profile.getAvatar())
                .bio(profile.getBio())
                .fullName(profile.getFullName())
                .build();
    }
}
