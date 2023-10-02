package com.webapp.socialmedia.security;

import com.webapp.socialmedia.dto.AuthenticationRequest;
import com.webapp.socialmedia.dto.AuthenticationRespone;
import com.webapp.socialmedia.dto.RegisterRequest;
import com.webapp.socialmedia.entity.RefreshToken;
import com.webapp.socialmedia.entity.Role;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.repository.RefreshTokenRepository;
import com.webapp.socialmedia.repository.UserRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationRespone register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        // thiếu kiểm tra email
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = generateRefreshToken(user);
        return AuthenticationRespone.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationRespone authenticate(AuthenticationRequest request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        User user;
        user = userOptional.orElseGet(() -> userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found")));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = generateRefreshToken(user);
        return AuthenticationRespone.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationRespone renewToken(String refreshToken) throws Exception {
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

        return AuthenticationRespone.builder()
                .accessToken(jwtToken)
                .refreshToken(id)
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
