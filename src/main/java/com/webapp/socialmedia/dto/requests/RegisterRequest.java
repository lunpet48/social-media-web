package com.webapp.socialmedia.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username must not be empty")
    private String username;
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email not valid")
    private String email;
    @NotBlank(message = "Password must not be empty")
    private String password;
    @NotBlank(message = "Fullname must not be empty")
    private String fullName;
    private int otpCode;

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public void setFullName(String fullName) {
        this.fullName = fullName.trim();
    }
}
