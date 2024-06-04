package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ChangePasswordRequest;
import com.webapp.socialmedia.dto.requests.ResetPasswordRequest;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;
import com.webapp.socialmedia.entity.Relationship;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.InvalidOTPException;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.RelationshipRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.IUserService;
import com.webapp.socialmedia.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    private final UserMapper userMapper;

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest, String userId) {

        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty())
        {
            throw new UsernameNotFoundException("User Not Found");
        }
        User user = userOptional.get();


        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(),
                user.getPassword()))
            throw new BadRequestException("Mật khẩu cũ không chính xác");
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        if (Integer.parseInt(resetPasswordRequest.getOtpCode()) != otpService.getOtp(OtpService.FORGOT_PASSWORD_KEY + resetPasswordRequest.getEmail()))
            throw new InvalidOTPException();

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public List<UserProfileResponse> getRecommendUsers(String id) {
        // sửa lại logic lấy user liên quan
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<User> users = userRepository.getRecommendUsers(id);
        List<UserProfileResponse> userProfileResponses = new ArrayList<>();
        List<Relationship> myRelationship = relationshipRepository.findByUserId(currentUser.getId());
        List<User> myRelationshipUser = new ArrayList<>();
        myRelationship.forEach(relationship -> {myRelationshipUser.add(relationship.getRelatedUser());});
        List<User> recommendUser = new ArrayList<>();
//        Lấy bạn bè của bản thân, với mỗi người bạn thì lấy bạn của người đó
        myRelationship.forEach(relationship -> {
             relationshipRepository.findByUserId(relationship.getRelatedUser().getId()).forEach(relationship1 -> {
                 if (!currentUser.getId().equals(relationship1.getRelatedUser().getId())) {
                     Optional<Relationship> temp = relationshipRepository.findByUserIdAndRelatedUserId(
                             currentUser.getId(),
                             relationship1.getRelatedUser().getId()
                     );

                     if (temp.isEmpty()) {
                         recommendUser.add(relationship1.getRelatedUser());
                     }
                 }
             });


        });
        recommendUser.forEach(user -> {
            userProfileResponses.add(userMapper.userToUserProfileResponse(user, currentUser.getId()));
        });

        return userProfileResponses;
    }


    @Override
    public void sendEmailForRegister(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isPresent())
            throw new BadRequestException("Email đã tồn tại");

        otpService.sendOtpRegister(email);
    }

    @Override
    public List<UserProfileResponse> search(String keyword) {
        if(keyword.trim().isEmpty())
            return new ArrayList<>();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> users = userRepository.searchUser(keyword);
        List<UserProfileResponse> userProfileResponses = new ArrayList<>();
        users.forEach(user -> {
            UserProfileResponse userProfileResponse = userMapper.userToUserProfileResponse(user, currentUser.getId());
            userProfileResponses.add(userProfileResponse);
        });
        return userProfileResponses;
    }
}
