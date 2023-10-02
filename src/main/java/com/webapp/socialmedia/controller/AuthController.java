package com.webapp.socialmedia.controller;

import com.webapp.socialmedia.dto.AuthenticationRequest;
import com.webapp.socialmedia.dto.AuthenticationRespone;
import com.webapp.socialmedia.dto.RegisterRequest;
import com.webapp.socialmedia.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationRespone> register(@RequestBody RegisterRequest request){
        AuthenticationRespone response = service.register(request);
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
                .body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationRespone> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationRespone response = service.authenticate(request);
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
                .body(response);
    }

    @PostMapping("/renew-token")
    public ResponseEntity<AuthenticationRespone> renewToken(@CookieValue(name = "refresh-token") String refreshToken) throws Exception {

        AuthenticationRespone response = service.renewToken(refreshToken);

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
                .body(response);
    }

}
