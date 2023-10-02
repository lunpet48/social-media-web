package com.webapp.socialmedia.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        // tại JwtAuthenticationFilter cần kiểm tra jwt đã hết hạn chưa rồi đặt attribute expired vào request
        final String expiredMsg = (String) request.getAttribute("expired");
        if(expiredMsg != null){
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", expiredMsg));

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().write(body);
        }
        else {
            // access denied
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", "access denied"));

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getOutputStream().write(body);
        }

    }
}
