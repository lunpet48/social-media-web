package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.entity.Profile;
import org.mapstruct.Mapper;

@Mapper
public interface IProfileMapper {
    Profile ProfileRequestToProfile(ProfileRequest profileRequest);

    ProfileResponse ProfileToProfileResponse(Profile profile);
}
