package com.webapp.socialmedia.dto.requests;

import com.webapp.socialmedia.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    private String userId;
    private String fullName;
    private Gender gender;
    private String address;
    private java.sql.Date dateOfBirth;
}
