package com.webapp.socialmedia.dto.requests;

import com.webapp.socialmedia.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    @NotBlank(message = "Họ và tên không được rỗng")
    private String fullName;
    private Gender gender;
    private String address;
    @Past(message = "Ngày sinh không hợp lệ")
    private java.sql.Date dateOfBirth;
}
