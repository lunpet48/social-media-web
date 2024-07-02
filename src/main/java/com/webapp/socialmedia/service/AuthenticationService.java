package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.AuthenticationRequest;
import com.webapp.socialmedia.dto.requests.RegisterRequest;
import com.webapp.socialmedia.dto.responses.AuthenticationResponse;
import com.webapp.socialmedia.entity.Profile;
import com.webapp.socialmedia.entity.RefreshToken;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.enums.Role;
import com.webapp.socialmedia.exceptions.BadRequestException;
import com.webapp.socialmedia.exceptions.InvalidOTPException;
import com.webapp.socialmedia.exceptions.UserExistException;
import com.webapp.socialmedia.exceptions.UserLockedException;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.ProfileRepository;
import com.webapp.socialmedia.repository.RefreshTokenRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.security.JwtService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final OtpService otpService;

    private final Validator validator;

    private final UserMapper userMapper;

    @Value("${app.config.refresh-token-age}")
    private String refreshTokenAge;

    public AuthenticationResponse register(RegisterRequest request) {
        //validate with validator
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        // check email, username unique
        if(userRepository.findByUsername(request.getUsername()).isPresent())
            throw new UserExistException("Username đã tồn tại");
        else if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new UserExistException(("Email đã tồn tại"));

        if(request.getOtpCode() == 0 ||
                request.getOtpCode() != otpService.getOtp(OtpService.REGISTER_KEY + request.getEmail()))
            throw new InvalidOTPException();

        User user = User
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        Profile profile = new Profile();
        profile.setFullName(request.getFullName());
        profile.setUser(user);
        user.setProfile(profile);

        profileRepository.save(profile);
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userMapper.userToUserProfileResponse(user))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //validate with validator
        Set<ConstraintViolation<AuthenticationRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        User user = userOptional.orElseGet(() -> userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Tài khoản hoặc mật khẩu không chính xác")));

        if(user.getIsLocked()) {
            throw new UserLockedException(user.getLogId());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userMapper.userToUserProfileResponse(user))
                .build();
    }

    public AuthenticationResponse renewToken(String refreshToken) throws Exception {
        RefreshToken oldRT = refreshTokenRepository.findById(refreshToken).orElseThrow(() -> new Exception("Invalid Token"));
        if (oldRT.getIsUsed()){
            List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByFamilyId(oldRT.getFamilyId());
            for (RefreshToken token : refreshTokens) {
                token.setIsRevoked(true);
            }
            refreshTokenRepository.saveAll(refreshTokens);

            throw new Exception("Token is used");
        }

        if(oldRT.getIsRevoked()){
            throw new Exception("Token is revoked");
        }
        oldRT.setIsUsed(true);

        User user = userRepository.findById(oldRT.getUser().getId()).orElseThrow(()->new UsernameNotFoundException("User Not Found"));

        String jwtToken = jwtService.generateToken(user);

        String id = UUID.randomUUID().toString();
        RefreshToken newRT = RefreshToken.builder()
                .id(id)
                .familyId(oldRT.getFamilyId())
                .user(user)
                .expireDate(new Date((new Date()).getTime() + Duration.parse(refreshTokenAge).toMillis() ))
                .build();
        refreshTokenRepository.save(oldRT);
        refreshTokenRepository.save(newRT);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(id)
                .user(userMapper.userToUserProfileResponse(user))
                .build();
    }
    private String generateRefreshToken(User user) {

        String id = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .id(id)
                .familyId(id)
                .user(user)
                .expireDate(new Date((new Date()).getTime() + Duration.parse(refreshTokenAge).toMillis() ))
                .build();
        refreshTokenRepository.save(refreshToken);
        return id;
    }

}
