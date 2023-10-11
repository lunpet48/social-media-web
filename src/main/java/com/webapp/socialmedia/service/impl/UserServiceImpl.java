package com.webapp.socialmedia.service.impl;

import com.webapp.socialmedia.dto.requests.ChangePasswordRequest;
import com.webapp.socialmedia.dto.requests.ResetPasswordRequest;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.InvalidOTPException;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.service.IUserService;
import com.webapp.socialmedia.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public <S extends User> S save(S entity) {
        return userRepository.save(entity);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(changePasswordRequest.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        changePasswordRequest.getOldPassword()
                )
        );

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        if (resetPasswordRequest.getOtpCode() != otpService.getOtp(OtpService.FORGOT_PASSWORD_KEY + resetPasswordRequest.getEmail()))
            throw new InvalidOTPException();

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        userRepository.save(user);
    }

}
