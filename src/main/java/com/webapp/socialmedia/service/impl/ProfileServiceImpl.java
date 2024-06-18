package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.Media;
import com.webapp.socialmedia.entity.Profile;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.RelationshipStatus;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.EmptyFileException;
import com.webapp.socialmedia.exceptions.UserNotFoundException;
import com.webapp.socialmedia.mapper.ProfileMapper;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.ProfileRepository;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.CloudService;
import com.webapp.socialmedia.service.IProfileService;
import com.webapp.socialmedia.utils.FileValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements IProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final CloudService cloudService;
    private final RelationshipRepository relationshipRepository;

    private final Validator validator;
    private final UserMapper userMapper;
    @Override
    public ProfileResponse update(ProfileRequest profileRequest, String userId) {

        //validate with validator
        Set<ConstraintViolation<ProfileRequest>> violations = validator.validate(profileRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Profile profile = profileRepository.findById(userId)
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
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Relationship> relationship = relationshipRepository.findByUserIdAndRelatedUserId(user.getId(), currentUser.getId());
        if (relationship.isPresent() && (relationship.get().getStatus().equals(RelationshipStatus.BLOCK) || relationship.get().getStatus().equals(RelationshipStatus.BLOCKED))) {
            return UserProfileResponse.builder().build();
        }

        return userMapper.userToUserProfileResponse(user, currentUser.getId());
    }

    @Override
    public ProfileResponse updateAvatar(String userId, MultipartFile multipartFile) {
        if(multipartFile.isEmpty())
            throw new EmptyFileException();

        if (!FileValidator.isImage(multipartFile)) {
            throw new BadRequestException("ảnh đại diện phải là hình ảnh");
        }
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

        if (!FileValidator.isImage(multipartFile)) {
            throw new BadRequestException("ảnh background phải là hình ảnh");
        }

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
