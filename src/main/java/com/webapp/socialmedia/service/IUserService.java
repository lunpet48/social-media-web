package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.ChangePasswordRequest;
import com.webapp.socialmedia.dto.requests.ResetPasswordRequest;
import com.webapp.socialmedia.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    <S extends User> S save(S entity);

    void changePassword(ChangePasswordRequest changePasswordRequest);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);
}
