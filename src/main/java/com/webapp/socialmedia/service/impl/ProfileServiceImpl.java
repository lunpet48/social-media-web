package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Profile;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipProfile;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.EmptyFileException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.exceptions.UserNotMatchTokenException;
import com.webapp.socialmedia.mapper.ProfileMapper;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.ProfileRepository;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.CloudService;
import com.webapp.socialmedia.service.IProfileService;
import com.webapp.socialmedia.validattion.serviceValidation.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserValidationService userValidationService;
//    private final IRelationshipService relationshipService;
    private final RelationshipRepository relationshipRepository;
    private final CloudService cloudService;

    @Override
    public ProfileResponse update(ProfileRequest profileRequest) {
        if(!userValidationService.isUserMatchToken(profileRequest.getUserId()))
            throw new UserNotMatchTokenException();
        Profile profile = profileRepository.findById(profileRequest.getUserId())
                .orElseThrow(UserNotFoundException::new);

        profile.setFullName(profileRequest.getFullName());
        profile.setGender(profileRequest.getGender());
        profile.setAddress(profileRequest.getAddress());
        profile.setDateOfBirth(profileRequest.getDateOfBirth());
        profileRepository.save(profile);
        return ProfileMapper.INSTANCE.ProfileToProfileResponse(profile);
    }

    @Override
    public UserProfileResponse get(String userId){
        User user = userRepository.findById(userId)
                .orElseGet(() -> userRepository.findByUsername(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found")));
        UserProfileResponse userProfileResponse = UserMapper.INSTANCE.userToUserProfileResponse(user);
        userProfileResponse.setFriendCount(relationshipRepository.findByUserIdAndStatus(user.getId(), RelationshipStatus.FRIEND).size());
        User currendUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user.getId().equals(currendUser.getId())){
            userProfileResponse.setRelationship(RelationshipProfile.SELF);
            return userProfileResponse;
        }

        if(relationshipRepository.findByUserIdAndRelatedUserIdAndStatus(user.getId(),currendUser.getId(), RelationshipStatus.PENDING).isPresent()){
            userProfileResponse.setRelationship(RelationshipProfile.INCOMMINGREQUEST);
            return userProfileResponse;
        }

        Optional<Relationship> relationshipOptional = relationshipRepository.findByUserIdAndRelatedUserId(currendUser.getId(), user.getId());
        if(relationshipOptional.isEmpty()){
            userProfileResponse.setRelationship(RelationshipProfile.STRANGER);
            return userProfileResponse;
        }

        Relationship relationship = relationshipOptional.get();

        userProfileResponse.setRelationship(RelationshipProfile.valueOf(relationship.getStatus().name()));
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
