package com.webapp.socialmedia.controller;

import com.webapp.socialmedia.dto.requests.AuthenticationRequest;
import com.webapp.socialmedia.dto.responses.AuthenticationResponse;
import com.webapp.socialmedia.dto.requests.RegisterRequest;
import com.webapp.socialmedia.dto.responses.ResponseDTO;
import com.webapp.socialmedia.service.AuthenticationService;
import com.webapp.socialmedia.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final OtpService otpService;
    @Value("${app.config.refresh-token-age}")
    private String refreshTokenAge;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        AuthenticationResponse response = authenticationService.register(request);
        ResponseCookie springCookie = ResponseCookie.from("refresh-token", response.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.parse(refreshTokenAge).toMillis())
//                .domain("example.com")
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(new ResponseDTO().success(response));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationService.authenticate(request);
        ResponseCookie springCookie = ResponseCookie.from("refresh-token", response.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60*60*24*7)
//                .domain("example.com")
                .build();
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(new ResponseDTO().success(response));
    }

    @PostMapping("/renew-token")
    public ResponseEntity<?> renewToken(@CookieValue(name = "refresh-token") String refreshToken) throws Exception {

        AuthenticationResponse response = authenticationService.renewToken(refreshToken);

        ResponseCookie springCookie = ResponseCookie.from("refresh-token", response.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60*60*24*7)
//                .domain("example.com")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(new ResponseDTO().success(response));
    }
    @PostMapping("/register/otp")
    public void sendOtpRegister(@RequestBody Map<String, String> email){
        otpService.sendOtpRegister(email.get("email"));
    }

    @PostMapping("/forgot-pasword/otp")
    public void sendOtpForgotPassword(@RequestBody String email){
        otpService.sendOtpForgotPassword(email);
    }
}
