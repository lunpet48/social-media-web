package com.webapp.socialmedia.service;

import com.webapp.socialmedia.dto.requests.AuthenticationRequest;
import com.webapp.socialmedia.dto.responses.AuthenticationResponse;
import com.webapp.socialmedia.dto.requests.RegisterRequest;
import com.webapp.socialmedia.entity.RefreshToken;
import com.webapp.socialmedia.enums.Role;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.exceptions.InvalidOTPException;
import com.webapp.socialmedia.exceptions.UserExistException;
import com.webapp.socialmedia.mapper.UserMapper;
import com.webapp.socialmedia.repository.RefreshTokenRepository;
import com.webapp.socialmedia.repository.UserRepository;
import com.webapp.socialmedia.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final OtpService otpService;

    public AuthenticationResponse register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent())
            throw new UserExistException("Username đã tồn tại");
        else if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new UserExistException(("Email đã tồn tại"));

        if(otpService.getOtp(OtpService.REGISTER_KEY + request.getEmail()) != request.getOtpCode())
            throw new InvalidOTPException();

        User user = User
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        User user = userOptional.orElseGet(() -> userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found")));

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
                .user(UserMapper.INSTANCE.userToUserResponse(user))
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

        RefreshToken newRT = new RefreshToken();
        String id = UUID.randomUUID().toString();
        newRT.setId(id);
        newRT.setFamilyId(oldRT.getFamilyId());
        newRT.setUser(user);
        refreshTokenRepository.save(oldRT);
        refreshTokenRepository.save(newRT);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(id)
                .user(UserMapper.INSTANCE.userToUserResponse(user))
                .build();
    }
    private String generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        String id = UUID.randomUUID().toString();
        refreshToken.setId(id);
        refreshToken.setFamilyId(id);
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);
        return id;
    }

}
