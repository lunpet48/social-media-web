package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.ChangePasswordRequest;
import com.webapp.socialmedia.dto.requests.ResetPasswordRequest;
import com.webapp.socialmedia.dto.responses.UserProfileResponse;

import java.util.List;

public interface IUserService {

    void changePassword(ChangePasswordRequest changePasswordRequest, String userId);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    List<UserProfileResponse> getRecommendUsers(String id);

    void sendEmailForRegister(String email);

    List<UserProfileResponse> search(String keyword);
}
