package com.webapp.socialmedia.dto.responses;

import com.webapp.socialmedia.enums.Gender;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String userId;
    private String bio;
    private String avatar;
    private String fullName;
    private Gender gender;
    private String address;
    private java.sql.Date dateOfBirth;
}
