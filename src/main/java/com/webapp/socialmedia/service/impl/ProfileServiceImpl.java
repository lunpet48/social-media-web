package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Profile;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.EmptyFileException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.exceptions.UserNotMatchTokenException;
import com.webapp.socialmedia.mapper.ProfileMapper;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.ProfileRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.CloudService;
import com.webapp.socialmedia.service.IProfileService;
import com.webapp.socialmedia.service.IRelationshipService;
import com.webapp.socialmedia.validattion.serviceValidation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserValidationService userValidationService;
    private final IRelationshipService relationshipService;
    private final CloudService cloudService;

    @Override
    public ProfileResponse update(ProfileRequest profileRequest) {
        if(!userValidationService.isUserMatchToken(profileRequest.getUserId()))
            throw new UserNotMatchTokenException();
        Profile profile = profileRepository.findById(profileRequest.getUserId())
                .orElseThrow(UserNotFoundException::new);

        profile.setFullName(profileRequest.getFullName());
        profileRepository.save(profile);
        return ProfileMapper.INSTANCE.ProfileToProfileResponse(profile);
    }

    @Override
    public UserProfileResponse get(String userId){
        User user = userRepository.findById(userId)
                .orElseGet(() -> userRepository.findByUsername(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found")));
        UserProfileResponse userProfileResponse = UserMapper.INSTANCE.userToUserProfileResponse(user);
        userProfileResponse.setFriendCount(relationshipService.findByUserIdAndStatus(user.getId(), RelationshipStatus.FRIEND).size());
        return userProfileResponse;
    }

    @Override
    public ProfileResponse updateAvatar(String userId, MultipartFile multipartFile) {
        if(multipartFile.isEmpty())
            throw new EmptyFileException();

        if(!userValidationService.isUserMatchToken(userId))
            throw new UserNotMatchTokenException();

        Profile profile = profileRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        try {
            Media media = cloudService.uploadFile(multipartFile);

            profile.setAvatar(media.getLink());
            profileRepository.save(profile);
            return ProfileMapper.INSTANCE.ProfileToProfileResponse(profile);

        } catch (IOException e) {
            throw new RuntimeException( "An error occur when save file",e);
        }
    }

    @Override
    public ProfileResponse updateBackground(String userId, MultipartFile multipartFile) {
        if(multipartFile.isEmpty())
            throw new EmptyFileException();

        if(!userValidationService.isUserMatchToken(userId))
            throw new UserNotMatchTokenException();

        Profile profile = profileRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        try {
            Media media = cloudService.uploadFile(multipartFile);

            profile.setBio(media.getLink());
            profileRepository.save(profile);
            return ProfileMapper.INSTANCE.ProfileToProfileResponse(profile);

        } catch (IOException e) {
            throw new RuntimeException( "An error occur when save file",e);
        }
    }

}
